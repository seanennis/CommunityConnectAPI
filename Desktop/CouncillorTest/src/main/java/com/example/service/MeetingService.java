package com.example.service;

import com.example.entity.Meeting;
import com.example.entity.Member;
import com.example.exception.ApiRequestException;
import com.example.repository.MeetingRepo;
import com.example.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingService {
    private final MeetingRepo meetingRepo;
    private final MemberRepo memberRepo;

    @Autowired
    public MeetingService(@Qualifier("MeetingRepo") MeetingRepo meetingRepo, @Qualifier("MemberRepo") MemberRepo memberRepo) {
        this.meetingRepo = meetingRepo;
        this.memberRepo = memberRepo;
    }

    public void insertMeeting(Meeting meeting) {
        this.meetingRepo.insert(meeting);
    }

    public List<Meeting> getAll() {
        return this.meetingRepo.findAll();
    }

    public Optional<Meeting> getMeetingById(String id) {
        return this.meetingRepo.findById(id);
    }

    public void deleteMeeting(String id) {
        this.meetingRepo.deleteById(id);
    }

    public void updateMeeting(String id, Meeting newMeeting) {
        this.meetingRepo.findById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        newMeeting.setId(id);
        meetingRepo.save(newMeeting);
    }

    public Optional<Meeting> getMeetingByName(String name) {
        return this.meetingRepo.findByName(name);
    }

    public void deleteMeetingByName(String name) {
        this.meetingRepo.deleteByName(name);
    }

    public void updateMeetingByName(String name, Meeting newMeeting) {
        this.meetingRepo.findByName(name).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));
        newMeeting.setId(this.meetingRepo.findByName(name).get().getId());
        meetingRepo.save(newMeeting);
    }

    public boolean isValid(Meeting meeting) {
        LocalDateTime dateTime = LocalDateTime.parse(meeting.getDateTime());
        int dayOfWeek = dateTime.getDayOfWeek().getValue() - 1;
        Optional<Member> member = Optional.ofNullable(memberRepo.findById(meeting.getMemberId()).orElseThrow(() ->
                new ApiRequestException("The member ID with whom this meeting is booked does not exist")));
        ArrayList<Integer>[] timeslots = member.get().getTimeslots();

        if(dateTime.isBefore(LocalDateTime.now()) || dateTime.isAfter(LocalDateTime.now().plusWeeks(2)))
            throw new ApiRequestException("The meeting date is outside of the 14 day window");

        if(dayOfWeek >= timeslots.length)
            throw new ApiRequestException("This meeting timeslot is not available");

        if(timeslots[dayOfWeek].contains(dateTime.getHour())) {
            member.get().removeTimeslot(dayOfWeek, dateTime.getHour());
            memberRepo.save(member.get());
        }
        else
            throw new ApiRequestException("This meeting timeslot is not available");

        return true;
    }
}

package communityconnect.service;

import communityconnect.entity.Meeting;
import communityconnect.entity.Member;
import communityconnect.exception.ApiRequestException;
import communityconnect.repository.MeetingRepo;
import communityconnect.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service which contains business logic for the Meeting Controller.
 * @author Sean Ennis O'Toole
 */

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

    public Optional<ArrayList<Meeting>> getMeetingByMemberID(String memberID) {
        return this.meetingRepo.findByMemberId(memberID);
    }

    public void deleteMeetingByMemberID(String memberID) {
        this.meetingRepo.deleteByMemberId(memberID);
    }

    public Optional<ArrayList<Meeting>> getMeetingByMemberIDandDatetime(String memberID, String dateTime) {
        return this.meetingRepo.findByMemberIdAndDateTime(memberID, dateTime);
    }

    public void deleteMeetingByMemberIDandDatetime(String memberID, String dateTime) {
        this.meetingRepo.deleteByMemberIdAndDateTime(memberID, dateTime);
    }

    //TODO ONLY FOR DEVELOPMENT USE DELETE BEFORE RELEASE
    public void deleteAllMeetings() {
        this.meetingRepo.deleteAll();
    }

    public boolean isValid(Meeting meeting) {
        LocalDateTime dateTime = LocalDateTime.parse(meeting.getDateTime());
        // within 28 days from now
        if(dateTime.isBefore(LocalDateTime.now()) || dateTime.isAfter(LocalDateTime.now().plusDays(27)))
            throw new ApiRequestException("The meeting date is outside of the 28 day window");

        Optional<Member> member = Optional.ofNullable(memberRepo.findById(meeting.getMemberId()).orElseThrow(() ->
                new ApiRequestException("The member ID with whom this meeting is booked does not exist")));
        HashMap<String, ArrayList<Float>> timeslots = member.get().getTimeslots(); // already checked if null above
        // strip meeting type held in decimal portion of floats by converting to int
        List<Integer> intTimeslots = timeslots.get(meeting.getDate()).stream().map(Float::intValue).collect(Collectors.toList());
        int dateTimeIndex = intTimeslots.indexOf(dateTime.getHour());
        boolean correctMeetingType = false;

        // If time slot is not in the array
        if(dateTimeIndex == -1)
            throw new ApiRequestException("This meeting timeslot is not available");

        // set correct meeting type (4 represents all meeting types)
        int type = Math.round((timeslots.get(meeting.getDate()).get(dateTimeIndex)-dateTime.getHour())*10);
        if(meeting.getType() == type || 4 == type)
            correctMeetingType = true;

        // time slot is present and available for meeting type
        if(intTimeslots.contains(dateTime.getHour()) && correctMeetingType) {
            member.get().bookTimeslot(meeting.getDate(), dateTimeIndex, (float)type);
            memberRepo.save(member.get());
        }
        else
            throw new ApiRequestException("This meeting timeslot is not available");

        return true;
    }

    // run daily at 12am
    @Scheduled(cron = "0 0 0 * * ?", zone = "Europe/Dublin")
    public void removeOldMeetings() {
        ArrayList<Meeting> meetings = (ArrayList<Meeting>) this.meetingRepo.findAll();

        for(Meeting meeting : meetings) {
            if(LocalDateTime.now().isAfter(LocalDateTime.parse(meeting.getDateTime())))
                meetingRepo.deleteById(meeting.getId());
        }
    }

    public Optional<ArrayList<Meeting>> getMeetingByStatusAndDate(int status, String date) {
        return meetingRepo.findByStatusAndDate(status, date);
    }

    public void deleteMeetingByStatusAndDate(int status, String date) {
        meetingRepo.deleteByStatusAndDate(status, date);
    }
}

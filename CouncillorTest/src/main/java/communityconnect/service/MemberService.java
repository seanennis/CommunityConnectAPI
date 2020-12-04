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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service which contains business logic for the Member Controller.
 * @author Sean Ennis O'Toole
 */

@Service
public class  MemberService {
    private final MemberRepo memberRepo;
    private final MeetingRepo meetingRepo;

    @Autowired
    public MemberService(@Qualifier("MemberRepo") MemberRepo memberRepo, @Qualifier("MeetingRepo") MeetingRepo meetingRepo) {
        this.memberRepo = memberRepo;
        this.meetingRepo = meetingRepo;
    }

    public void insertMember(Member member) {
        if(this.memberRepo.findByName(member.getName()).isPresent())
            throw new ApiRequestException("There is already a member by this name," +
                    " try using a put request if you wish to edit this member");
        else
            this.memberRepo.insert(member);
    }

    public List<Member> getAll() {
        return this.memberRepo.findAll();
    }

    public Optional<Member> getMemberById(String id) {
        return this.memberRepo.findById(id);
    }

    public void deleteMember(String id) {
        this.memberRepo.deleteById(id);
    }

    public void updateMember(String id, Member newMember) {
        this.memberRepo.findById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        newMember.setId(id);
        memberRepo.save(newMember);
    }

    public Optional<Member> getMemberByName(String name) {
        return this.memberRepo.findByName(name);
    }

    public void deleteMemberByName(String name) {
        this.memberRepo.deleteByName(name);
    }

    public void updateMemberByName(String name, Member newMember) {
        this.memberRepo.findByName(name).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));
        newMember.setId(this.memberRepo.findByName(name).get().getId());
        memberRepo.save(newMember);
    }

    public void deleteAllMembers() {
        this.memberRepo.deleteAll();
    }

    // run daily at 5 minutes past 12
    // resets all values for yesterdays timeslots
    @Scheduled(cron = "5 0 0 * * ?", zone = "Europe/Dublin")
    public void renewCalender() {
        ArrayList<Member> members = (ArrayList<Member>) this.memberRepo.findAll();
        // yesterdays date
        int index = LocalDateTime.now().getDayOfWeek().getValue() - 2;

        for(Member member : members) {
            member.setTimeslotsDay(member.getDefaultTimeslots()[index], index);
            memberRepo.save(member);
        }
    }

    // changes times in timeslot of bookings which have not been cancelled to .0 to prevent double bookings
    // returns updated timeslot
    public ArrayList<Float>[] updateTimeslots(ArrayList<Float>[] timeslots, Member member) {
        Optional<ArrayList<Meeting>> meetings = meetingRepo.findByMemberId(member.getId());
        ArrayList<Meeting> meetingsList;

        if(meetings.isPresent())
            meetingsList = meetings.get();
        else
            return null;

        for(Meeting meeting : meetingsList) {
            LocalDateTime dateTime = LocalDateTime.parse(meeting.getDateTime());
            int dayOfWeek = dateTime.getDayOfWeek().getValue() - 1;
            for(int i = 21; i >= 7; i -= 7) {
                if (dateTime.isAfter(LocalDateTime.now().plusDays(i - 1))) {
                    dayOfWeek += i;
                    break;
                }
            }

            List<Integer> intTimeslots =
                    timeslots[dayOfWeek].stream().map(Float::intValue).collect(Collectors.toList());
            if(intTimeslots.contains(dateTime.getHour())) {
                int index = intTimeslots.indexOf(dateTime.getHour());
                timeslots[dayOfWeek].set(index, (float)dateTime.getHour());
            }
        }


        return timeslots;
    }

    // returns all meetings which have been cancelled as a result of the timeslot changes
    public ArrayList<Meeting> findOverwrittenMeetings(ArrayList<Float>[] timeslots, Member member) {
        Optional<ArrayList<Meeting>> meetings = meetingRepo.findByMemberId(member.getId());
        ArrayList<Meeting> meetingsList;
        ArrayList<Meeting> closedMeetings = new ArrayList<>();

        if(meetings.isPresent())
            meetingsList = meetings.get();
        else
            return null;

        for(Meeting meeting : meetingsList) {
            LocalDateTime dateTime = LocalDateTime.parse(meeting.getDateTime());
            int dayOfWeek = dateTime.getDayOfWeek().getValue() - 1;
            for(int i = 21; i >= 7; i -= 7) {
                if (dateTime.isAfter(LocalDateTime.now().plusDays(i - 1))) {
                    dayOfWeek += i;
                    break;
                }
            }

            List<Integer> intTimeslots =
                    timeslots[dayOfWeek].stream().map(Float::intValue).collect(Collectors.toList());
            if(!intTimeslots.contains(dateTime.getHour())) {
                closedMeetings.add(meeting);
            }
        }

        return closedMeetings;
    }
}

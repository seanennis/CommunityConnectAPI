package communityconnect.service;

import communityconnect.entity.Meeting;
import communityconnect.entity.Member;
import communityconnect.entity.MemberLogin;
import communityconnect.exception.ApiRequestException;
import communityconnect.repository.MeetingRepo;
import communityconnect.repository.MemberLoginRepo;
import communityconnect.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service which contains business logic for the Member Controller.
 * @author Sean Ennis O'Toole
 */

@Service
public class  MemberService {
    private final MemberRepo memberRepo;
    private final MeetingRepo meetingRepo;
    private final MemberLoginRepo memberLoginRepo;

    @Autowired
    public MemberService(@Qualifier("MemberRepo") MemberRepo memberRepo, @Qualifier("MeetingRepo") MeetingRepo meetingRepo,
                         @Qualifier("MemberLoginRepo") MemberLoginRepo memberLoginRepo) {
        this.memberRepo = memberRepo;
        this.meetingRepo = meetingRepo;
        this.memberLoginRepo = memberLoginRepo;
    }

    public Map<String, String> insertMember(Member member) {
        if(member.getDefaultTimeslots().length != 7)
            throw new ApiRequestException("Length of defaultTimeslots must equal 7");
        if(this.memberRepo.findByName(member.getName()).isPresent())
            throw new ApiRequestException("There is already a member by this name," +
                    " try using a put request if you wish to edit this member");

        this.memberRepo.insert(member);
        Optional<Member> newMember = this.memberRepo.findByName(member.getName());
        String username = member.getName().toLowerCase().replaceAll("\\s", "").replaceAll("'", "");
        String password = randomPassword(8);
        MemberLogin memberLogin = new MemberLogin(username, newMember.get().getId(), password);
        this.memberLoginRepo.insert(memberLogin);

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("Username", username);
        credentials.put("Password", password);
        return credentials;
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

    //TODO ONLY FOR DEVELOPMENT USE DELETE BEFORE RELEASE
    public void deleteAllMembers() {
        this.memberRepo.deleteAll();
    }

    // run daily at 5 minutes past 12
    // resets all values for yesterdays timeslots
    @Scheduled(cron = "5 0 0 * * ?", zone = "Europe/Dublin")
    public void renewCalender() {
        ArrayList<Member> members = (ArrayList<Member>) this.memberRepo.findAll();
        for(Member member : members) {
            member.updateTimeslots();
            memberRepo.save(member);
        }
    }

    // changes times in timeslot of bookings which have not been cancelled to .0 to prevent double bookings
    // returns updated timeslot
    public HashMap<String, ArrayList<Float>> updateTimeslots(ArrayList<Float>[] defaultTimeslots, Member member) {
        // generate timeslots for the next 4 weeks
        HashMap<String, ArrayList<Float>> newTimeslots = member.generateTimeslots(defaultTimeslots);
        // find all meetings with this member
        Optional<ArrayList<Meeting>> meetings = meetingRepo.findByMemberId(member.getId());
        ArrayList<Meeting> meetingsList;

        if(meetings.isPresent())
            meetingsList = meetings.get();
        else
            return null;

        // update newTimeslots to include these meetings
        for(Meeting meeting : meetingsList) {
            LocalDateTime dateTime = LocalDateTime.parse(meeting.getDateTime());

            List<Integer> intTimeslots =
                    newTimeslots.get(meeting.getDate()).stream().map(Float::intValue).collect(Collectors.toList());
            if(intTimeslots.contains(dateTime.getHour())) {
                int index = intTimeslots.indexOf(dateTime.getHour());
                newTimeslots.get(meeting.getDate()).set(index, (float)dateTime.getHour());
            }
        }

        return newTimeslots;
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

            List<Integer> intTimeslots =
                    timeslots[dayOfWeek].stream().map(Float::intValue).collect(Collectors.toList());
            if(!intTimeslots.contains(dateTime.getHour())) {
                closedMeetings.add(meeting);
            }
        }

        return closedMeetings;
    }

    public static String randomPassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}

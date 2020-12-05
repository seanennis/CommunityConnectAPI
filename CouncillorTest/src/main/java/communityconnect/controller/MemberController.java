package communityconnect.controller;

import communityconnect.entity.Meeting;
import communityconnect.entity.Member;
import communityconnect.entity.MemberLogin;
import communityconnect.entity.Timeslot;
import communityconnect.exception.ApiRequestException;
import communityconnect.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for end points involving the member collection of the Community Connect database.
 * @author Sean Ennis O'Toole
 */

@RequestMapping("api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public Map<String, String> insertMember(@Valid @NonNull @RequestBody Member member) {
        return this.memberService.insertMember(member);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return this.memberService.getAll();
    }

    @GetMapping(path = "/id/{id}")
    public Member getMemberById(@PathVariable("id") String id) {
        return this.memberService.getMemberById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
    }

    @GetMapping(path = "/timeslots/id/{id}")
    public ArrayList<Float>[] getTimeslotsById(@PathVariable("id") String id) {
        Member member = this.memberService.getMemberById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        return member.getTimeslots();
    }

    @PutMapping(path = "/active/{id}")
    public void isActive(@PathVariable("id") String id) {
        Member member = this.memberService.getMemberById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        member.setActive(true);
        this.memberService.updateMember(id, member);
    }

    @GetMapping(path = "/active/{id}")
    public Boolean getIsActive(@PathVariable("id") String id) {
        Member member = this.memberService.getMemberById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        return member.isActive();
    }

    @DeleteMapping(path = "/id/{id}")
    public void deleteMemberById(@PathVariable("id") String id) {
        this.memberService.deleteMember(id);
    }

    @PutMapping(path = "/id/{id}")
    public void update(@Valid @NonNull @RequestBody Member member, @PathVariable("id") String id) {
        Member oldMember = this.memberService.getMemberById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        member.setTimeslots(oldMember.getTimeslots());
        member.setDefaultTimeslots(oldMember.getDefaultTimeslots(), member);
        this.memberService.updateMember(id, member);
    }

    @PutMapping(path = "/meetingid/{id}")
    public void addMeetingId(@RequestBody String meetingId, @PathVariable("id") String id) {
        Member member = this.memberService.getMemberById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        member.addMeetingID(meetingId);
        this.memberService.updateMember(id, member);
    }

    @PutMapping(path = "/timeslots/id/{id}")
    public ArrayList<Meeting> updateTimeslotsById(@RequestBody Timeslot timeslots, @PathVariable("id") String id) {
        Member member = this.memberService.getMemberById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        ArrayList<Float>[] newTimeslots = memberService.updateTimeslots(timeslots.getTimeslots(), member);
        ArrayList<Meeting> closedMeetings = memberService.findOverwrittenMeetings(timeslots.getTimeslots(), member);
        if(newTimeslots != null)
            member.setTimeslots(newTimeslots);
        else   // if no meetings are booked change timeslots as usual
            member.setTimeslots(timeslots.getTimeslots());
        member.setDefaultTimeslots(timeslots.getTimeslots(), member);
        this.memberService.updateMember(id, member);
        
        return closedMeetings;
    }

    @GetMapping(path = "/name/{name}")
    public Member getMemberByName(@PathVariable("name") String name) {
        return this.memberService.getMemberByName(name).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));
    }

    @GetMapping(path = "/timeslots/name/{name}")
    public ArrayList<Float>[] getTimeslotsByName(@PathVariable("name") String name) {
        Member member = this.memberService.getMemberByName(name).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
        return member.getTimeslots();
    }

    @DeleteMapping(path = "/name/{name}")
    public void deleteMemberByName(@PathVariable("name") String name) {
        this.memberService.deleteMemberByName(name);
    }

    //TODO ONLY FOR DEVELOPMENT USE DELETE BEFORE RELEASE
    @DeleteMapping(path = "/clearAll")
    public void deleteAllMembers() {
        this.memberService.deleteAllMembers();
    }
}

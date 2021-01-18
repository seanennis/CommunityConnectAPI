package communityconnect.controller;

import communityconnect.entity.Meeting;
import communityconnect.exception.ApiRequestException;
import communityconnect.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for end points involving the booking collection of the Community Connect database.
 * @author Sean Ennis O'Toole
 */

@RequestMapping("api/booking")
@RestController
public class MeetingController {
    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }
    @PostMapping
    public void insertMeeting(@Valid @NonNull @RequestBody Meeting meeting) {
        if(meetingService.isValid(meeting))
            this.meetingService.insertMeeting(meeting);
    }

    @GetMapping
    public List<Meeting> getAllMeetings() {
        return this.meetingService.getAll();
    }

    @GetMapping(path = "/id/{id}")
    public Meeting getMeetingById(@PathVariable("id") String id) {
        return this.meetingService.getMeetingById(id).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this ID"));
    }

    @DeleteMapping(path = "/id/{id}")
    public void deleteMemberById(@PathVariable("id") String id) {
        this.meetingService.deleteMeeting(id);
    }

    @PutMapping(path = "/id/{id}")
    public void update(@Valid @NonNull @RequestBody Meeting meeting, @PathVariable("id") String id) {
        if(meetingService.isValid(meeting))
            this.meetingService.updateMeeting(id, meeting);
    }

    @GetMapping(path = "/name/{name}")
    public Meeting getMeetingByName(@PathVariable("name") String name) {
        return this.meetingService.getMeetingByName(name).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));
    }

    @DeleteMapping(path = "/name/{name}")
    public void deleteMeetingByName(@PathVariable("name") String name) {
        this.meetingService.deleteMeetingByName(name);
    }

    @GetMapping(path = "/memberID/{memberID}")
    public ArrayList<Meeting> getMeetingByMemberID(@PathVariable("memberID") String memberID) {
        return this.meetingService.getMeetingByMemberID(memberID).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));
    }

    @DeleteMapping(path = "/memberID/{memberID}")
    public void deleteMeetingByMemberID(@PathVariable("memberID") String memberID) {
        this.meetingService.deleteMeetingByMemberID(memberID);
    }

    @GetMapping(path = "/IDandDatetime/{memberID}/{dateTime}")
    public ArrayList<Meeting> getMeetingByIDandDate(@PathVariable("memberID") String memberID, @PathVariable("dateTime") String dateTime) {
        return this.meetingService.getMeetingByMemberIDandDatetime(memberID, dateTime).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));
    }

    @DeleteMapping(path = "/IDandDatetime/{memberID}/{dateTime}")
    public void deleteMeetingByName(@PathVariable("memberID") String memberID, @PathVariable("dateTime") String dateTime) {
        this.meetingService.deleteMeetingByMemberIDandDatetime(memberID, dateTime);
    }

    @GetMapping(path = "/dateAndStatus/{date}/{status}")
    public Optional<ArrayList<Meeting>> getMeetingStatusandDate(@PathVariable("date") String date, @PathVariable("status") int status) {
        return this.meetingService.getMeetingByStatusAndDate(status, date);
    }

    @DeleteMapping(path = "/dateAndStatus/{date}/{status}")
    public void deleteMeetingStatusandDate(@PathVariable("date") String date, @PathVariable("status") int status) {
        this.meetingService.deleteMeetingByStatusAndDate(status, date);
    }

    @PutMapping(path = "/status/{id}")
    public void update(@RequestBody int status, @PathVariable("id") String id) {
            this.meetingService.updateMeetingStatus(id, status);
    }

    //TODO ONLY FOR DEVELOPMENT USE DELETE BEFORE RELEASE
    @DeleteMapping(path = "/clearAll")
    public void deleteAllMeetings() {
        this.meetingService.deleteAllMeetings();
    }
}

package com.example.controller;

import com.example.entity.Meeting;
import com.example.exception.ApiRequestException;
import com.example.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PutMapping(path = "/name/{name}")
    public void updateByName(@Valid @NonNull @RequestBody Meeting meeting, @PathVariable("name") String name) {
        if(meetingService.isValid(meeting))
            this.meetingService.updateMeetingByName(name, meeting);
    }
}

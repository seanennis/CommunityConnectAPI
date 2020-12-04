package com.example.controller;

import com.example.entity.Member;
import com.example.exception.ApiRequestException;
import com.example.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public void insertMember(@Valid @NonNull @RequestBody Member member) {
        this.memberService.insertMember(member);
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

    @DeleteMapping(path = "/id/{id}")
    public void deleteMemberById(@PathVariable("id") String id) {
        this.memberService.deleteMember(id);
    }

    @PutMapping(path = "/id/{id}")
    public void update(@Valid @NonNull @RequestBody Member member, @PathVariable("id") String id) {
        this.memberService.updateMember(id, member);
    }

    @GetMapping(path = "/name/{name}")
    public Member getMemberByName(@PathVariable("name") String name) {
        return this.memberService.getMemberByName(name).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));
    }

    @DeleteMapping(path = "/name/{name}")
    public void deleteMemberByName(@PathVariable("name") String name) {
        this.memberService.deleteMemberByName(name);
    }

    @PutMapping(path = "/name/{name}")
    public void updateByName(@Valid @NonNull @RequestBody Member member, @PathVariable("name") String name) {
        this.memberService.updateMemberByName(name, member);
    }
}

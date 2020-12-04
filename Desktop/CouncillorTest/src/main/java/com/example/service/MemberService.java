package com.example.service;

import com.example.entity.Meeting;
import com.example.entity.Member;
import com.example.exception.ApiRequestException;
import com.example.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class  MemberService {
    private final MemberRepo memberRepo;

    @Autowired
    public MemberService(@Qualifier("MemberRepo") MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    public void insertMember(Member member) {
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
}

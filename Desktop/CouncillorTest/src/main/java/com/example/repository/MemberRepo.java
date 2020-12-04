package com.example.repository;

import com.example.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("MemberRepo")
public interface MemberRepo extends MongoRepository<Member, String> {
    Optional<Member> findById(String id);

    Optional<Member> findByName(String name);

    void deleteByName(String name);
}

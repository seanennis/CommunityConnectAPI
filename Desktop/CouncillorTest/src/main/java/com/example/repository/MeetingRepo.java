package com.example.repository;

import com.example.entity.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("MeetingRepo")
public interface MeetingRepo extends MongoRepository<Meeting, String> {
    Optional<Meeting> findById(String id);

    Optional<Meeting> findByName(String name);

    void deleteByName(String name);
}

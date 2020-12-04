package communityconnect.repository;

import communityconnect.entity.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Repository for the Meeting Service.
 * @author Sean Ennis O'Toole
 */

@Repository("MeetingRepo")
public interface MeetingRepo extends MongoRepository<Meeting, String> {
    Optional<Meeting> findById(String id);

    Optional<Meeting> findByName(String name);

    Optional<ArrayList<Meeting>> findByMemberId(String memberId);

    void deleteByName(String name);
}

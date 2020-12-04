package communityconnect.repository;

import communityconnect.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the Member Service.
 * @author Sean Ennis O'Toole
 */

@Repository("MemberRepo")
public interface MemberRepo extends MongoRepository<Member, String> {
    Optional<Member> findById(String id);

    Optional<Member> findByName(String name);

    void deleteByName(String name);
}

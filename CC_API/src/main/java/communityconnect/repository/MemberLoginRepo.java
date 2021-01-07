package communityconnect.repository;

import communityconnect.entity.MemberLogin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the Member Service.
 * @author Sean Ennis O'Toole
 */

@Repository("MemberLoginRepo")
public interface MemberLoginRepo extends MongoRepository<MemberLogin, String> {
    Optional<MemberLogin> findByUsername(String username);
}

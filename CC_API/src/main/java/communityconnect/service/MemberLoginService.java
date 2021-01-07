package communityconnect.service;

import communityconnect.entity.MemberLogin;
import communityconnect.repository.MemberLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberLoginService {
    private final MemberLoginRepo memberLoginRepo;

    @Autowired
    public MemberLoginService(@Qualifier("MemberLoginRepo") MemberLoginRepo memberLoginRepo) {
        this.memberLoginRepo = memberLoginRepo;
    }

    public Optional<MemberLogin> findByUsername(String username) {
        return this.memberLoginRepo.findByUsername(username);
    }

    //TODO ONLY FOR DEVELOPMENT USE DELETE BEFORE RELEASE
    public void deleteAllMembers() {
        memberLoginRepo.deleteAll();
    }
}

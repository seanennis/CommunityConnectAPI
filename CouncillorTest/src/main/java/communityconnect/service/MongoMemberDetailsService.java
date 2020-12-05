package communityconnect.service;

import communityconnect.entity.MemberLogin;
import communityconnect.repository.MemberLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class MongoMemberDetailsService implements UserDetailsService {
    @Autowired
    private MemberLoginRepo repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberLogin> memberLogin = repository.findByUsername(username);

        if(!memberLogin.isPresent()) {
            throw new UsernameNotFoundException("Member not found");
        }

        List authorities = Arrays.asList(new SimpleGrantedAuthority("user"));

        return new User(memberLogin.get().getUsername(), memberLogin.get().getPassword(), authorities);
    }
}

package communityconnect.controller;

import communityconnect.entity.Meeting;
import communityconnect.entity.Member;
import communityconnect.entity.MemberLogin;
import communityconnect.entity.Timeslot;
import communityconnect.exception.ApiRequestException;
import communityconnect.service.MemberLoginService;
import communityconnect.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for end points involving the member collection of the Community Connect database.
 * @author Sean Ennis O'Toole
 */

@RequestMapping("api/memberLogin")
@RestController
public class MemberLoginController {
    private final MemberLoginService memberLoginService;

    @Autowired
    public MemberLoginController(MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @GetMapping(path = "{username}")
    public String getMemberId(@PathVariable("username") String username, Principal principal) {
        MemberLogin memberLogin = this.memberLoginService.findByUsername(username).orElseThrow(() ->
                new ApiRequestException("Cannot find member with this name"));

        if(username.equals(principal.getName())) {
            return memberLogin.getMemberId();
        }
        else
            throw new ApiRequestException("Incorrect credentials");
    }

    //TODO ONLY FOR DEVELOPMENT USE DELETE BEFORE RELEASE
    @DeleteMapping(path = "/clearAll")
    public void deleteAllMembers() {
        this.memberLoginService.deleteAllMembers();
    }
}

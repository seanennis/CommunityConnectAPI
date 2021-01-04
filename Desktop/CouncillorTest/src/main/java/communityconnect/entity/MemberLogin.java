package communityconnect.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;

@Document(collection = "membersLogins")
public class MemberLogin {
    @Id
    private String id;
    private String username;
    private String password;
    private String memberId;

    public MemberLogin() {
    }

    public MemberLogin(@NotBlank String username, @NotBlank String memberId, @NotBlank String password) {
        this.username = username;
        this.memberId = memberId;
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}

package communityconnect.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

/**
 * Entity describing a meeting document for the Community Connect Mongo database.
 * @author Sean Ennis O'Toole
 */

@Document(collection = "bookings")
public class Meeting {
    @Id
    private String id;
    @NotBlank
    private final String dateTime;
    @NonNull
    private final int type; // 1->In Person; 2->Phone; 3->Video Call
    @NonNull
    private final int status; // 0->Unbooked; 1->Accepted; 2->Declined; 3->Pending; 4->Disabled
    @NotBlank
    private String name;
    @NotBlank
    private final String subject;
    @NotBlank
    private final String email;
    @NotBlank
    private final String memberId;

    private final String location;

    public Meeting(@JsonProperty("dateTime") String dateTime, @JsonProperty("type")
            int type, @JsonProperty("status") int status, @JsonProperty("name")
                           String name, @JsonProperty("subject") String subject, @JsonProperty("email") String email,
                   @JsonProperty("memberId") String memberId, @JsonProperty("location") String location) {
        this.dateTime = dateTime;
        this.type = type;
        this.status = status;
        this.name = name;
        this.subject = subject;
        this.email = email;
        this.memberId = memberId;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getMemberId() {
        return memberId;
    }
}

package communityconnect.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Random;

/**
 * Entity describing a member document for the Community Connect Mongo database.
 * @author Sean Ennis O'Toole
 */

@Document(collection = "members")
public class Member {
    @Id
    private String id;
    @NotBlank
    final private String position; // councillor, TD, senator
    @NotBlank
    final private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    // Array contains 7 lists for each of the 7 days of the week (monday == 0; Sunday == 6)
    // floats represent start time of available meeting times on the hour (24 hour clock)
    // number after decimal place represents meeting type
    // (0.0 == booked, 0.1 == In person, 0.2 == phone call, 0.3 == video call, 0.4 == all)
    private ArrayList<Float>[] timeslots;
    private ArrayList<Float>[] defaultTimeslots;
    private ArrayList<String> meetingIDs;

    public Member(@JsonProperty("position") String position,
                  @JsonProperty("name") String name,
                  @JsonProperty("timeslots") ArrayList<Float>[] timeslots) {
        this.position = position;
        this.name = name;
        this.timeslots = timeslots;
        this.defaultTimeslots = timeslots;
        this.username = name.toLowerCase().replaceAll("\\s", "").replaceAll("'", "");
        this.password = randomPassword(8);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Float>[] getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(ArrayList<Float>[] timeslots) {
        this.timeslots = timeslots;
    }

    public void setTimeslotsDay(ArrayList<Float> timeslotDay, int index) {
        this.timeslots[index] = timeslotDay;
    }

    public void bookTimeslot(int days, int index, float type) {
        timeslots[days].set(index, (float)Math.round(timeslots[days].get(index) - type/10));
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public ArrayList<String> getMeetingIDs() {
        return meetingIDs;
    }

    public void setMeetingIDs(ArrayList<String> meetingIDs) {
        this.meetingIDs = meetingIDs;
    }

    public ArrayList<Float>[] getDefaultTimeslots() {
        return defaultTimeslots;
    }

    public void setDefaultTimeslots(ArrayList<Float>[] defaultTimeslots, Member member) {
        this.defaultTimeslots = defaultTimeslots;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDefaultTimeslots(ArrayList<Float>[] defaultTimeslots) {
        this.defaultTimeslots = defaultTimeslots;
    }

    public static String randomPassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}


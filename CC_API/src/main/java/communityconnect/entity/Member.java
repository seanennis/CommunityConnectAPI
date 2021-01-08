package communityconnect.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

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
    // hashmap key is date in datetime format
    // floats represent start time of available meeting times on the hour (24 hour clock)
    // number after decimal place represents meeting type
    // (0.0 == booked, 0.1 == In person, 0.2 == phone call, 0.3 == video call, 0.4 == all)
    private HashMap<String, ArrayList<Float>> timeslots;
    // Array contains 7 lists for each of the 7 days of the week (monday == 0; Sunday == 6)
    private ArrayList<Float>[] defaultTimeslots;
    private ArrayList<String> meetingIDs;
    private boolean active;

    public Member(@JsonProperty("position") String position,
                  @JsonProperty("name") String name,
                  @JsonProperty("defaultTimeslots") ArrayList<Float>[] defaultTimeslots) {
        this.position = position;
        this.name = name;
        this.defaultTimeslots = defaultTimeslots;
        this.timeslots = generateTimeslots(defaultTimeslots);
        this.active = false;
    }

    public HashMap<String, ArrayList<Float>> generateTimeslots(ArrayList<Float>[] timeslots) {
        HashMap<String, ArrayList<Float>> timeslotMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime today = LocalDateTime.now();
        int startIndex = today.getDayOfWeek().getValue() - 1;

        for(int i = 0; i < 28; i++)
            timeslotMap.put(today.plusDays(i).format(formatter), timeslots[(startIndex+i)%7]);

        return timeslotMap;
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

    public HashMap<String, ArrayList<Float>> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(HashMap<String, ArrayList<Float>> timeslots) {
        this.timeslots = timeslots;
    }

    public void updateTimeslots() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // yesterdays date
        timeslots.remove(LocalDateTime.now().minusDays(1).format(formatter));
        // 4 weeks from now
        LocalDateTime newDate = LocalDateTime.now().plusDays(27);
        int index = newDate.getDayOfWeek().getValue() - 1;
        timeslots.put(newDate.format(formatter), this.defaultTimeslots[index]);
    }

    public void bookTimeslot(String date, int index, float type) {
        ArrayList<Float> timeList = this.timeslots.get(date);
        timeList.set(index, (float)Math.round(timeList.get(index) - type/10));
        this.timeslots.put(date, timeList);
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
        this.defaultTimeslots = member.getDefaultTimeslots();
    }

    public void setDefaultTimeslots(ArrayList<Float>[] defaultTimeslots) {
        this.defaultTimeslots = defaultTimeslots;
    }

    public void addMeetingID(String meetingId) {
        meetingIDs.add(meetingId);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}


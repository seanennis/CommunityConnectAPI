package com.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Document(collection = "members")
public class Member {
    @Id
    private String id;
    @NotBlank
    final private String position; // councillor, TD, senator
    @NotBlank
    final private String name;
    // Array contains 7 lists for each of the 7 days of the week (monday == 0; Sunday == 6)
    // lists represents start time of available meeting times on the hour (24 hour clock)
    private ArrayList<Integer>[] timeslots;

    public Member(@JsonProperty("position") String position,
                  @JsonProperty("name") String name,
                  @JsonProperty("timeslots") ArrayList<Integer>[] timeslots) {
        this.position = position;
        this.name = name;
        this.timeslots = timeslots;
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

    public ArrayList<Integer>[] getTimeslots() {
        return timeslots;
    }

    public void removeTimeslot(int days, Integer timeslot) {
        timeslots[days].remove(timeslot);
    }
}


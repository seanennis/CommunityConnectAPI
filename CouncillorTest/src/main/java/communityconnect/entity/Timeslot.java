package communityconnect.entity;

import java.util.ArrayList;

/**
 * Entity describing a timelsot object for a Member document.
 * @author Sean Ennis O'Toole
 */

public class Timeslot {
    ArrayList<Float>[] timeslots;

    public ArrayList<Float>[] getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(ArrayList<Float>[] timeslots) {
        this.timeslots = timeslots;
    }
}

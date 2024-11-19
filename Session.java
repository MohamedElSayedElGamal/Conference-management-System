import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Session {
    private String sessionID;
    private String sessionName;
    private Speaker speaker;
    private Date date;
    private String time;
    private String room;
    private List<Attendee> attendees;

    // Constructor
    public Session(String sessionID, String sessionName, Speaker speaker, Date date, String time, String room) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.speaker = speaker;
        this.date = date;
        this.time = time;
        this.room = room;
        this.attendees = new ArrayList<>();
    }

    // Method to register attendance
    public void registerAttendance(Attendee attendee) {
        attendees.add(attendee);
    }

    // Method to get session details
    public String getSessionDetails() {
        return "Session ID: " + sessionID + ", Name: " + sessionName + ", Speaker: " + speaker.getName() +
               ", Date: " + date + ", Time: " + time + ", Room: " + room;
    }

    // Getters
    public String getSessionID() {
        return sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }
}

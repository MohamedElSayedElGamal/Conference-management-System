import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conference {
    private String name;
    private Date startDate;
    private Date endDate;
    private List<Session> sessions;
    private List<Attendee> attendees;

    // Constructor
    public Conference(String name, Date startDate, Date endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessions = new ArrayList<>();
        this.attendees = new ArrayList<>();
    }

    // Method to open a session
    public void openSession(Session session) {
        sessions.add(session);
    }

    // Method to register an attendee
    public void registerAttendee(Attendee attendee) {
        attendees.add(attendee);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }
}

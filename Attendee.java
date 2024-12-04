import java.util.*;

public class Attendee {
    private String attendeeID;
    private String name;
    private String email;
    private List<Session> schedule;

    public Attendee(String attendeeID, String name, String email) {
        this.attendeeID = attendeeID;
        this.name = name;
        this.email = email;
        this.schedule = new ArrayList<>();
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Session> getSchedule() {
        return schedule;
    }

    public void addToSchedule(Session session) {
        schedule.add(session);
    }

    public void removeFromSchedule(Session session) {
        schedule.remove(session);
    }

    public String generateCertificate() {
        return "Certificate of Attendance for " + name + " for attending " + schedule.size() + " sessions.";
    }

    public static List<Attendee> loadAttendees() throws Exception {
        return AttendeeDatabase.loadAttendees();
    }

    public static void saveAttendees(List<Attendee> attendees) throws Exception {
        AttendeeDatabase.saveAttendees(attendees);
    }

    public static void addAttendee(Attendee attendee) throws Exception {
        AttendeeDatabase.addAttendee(attendee);
    }
}

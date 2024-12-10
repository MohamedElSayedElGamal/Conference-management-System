import java.text.SimpleDateFormat;
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
    private List<Feedback> feedbackList;

    // Full Constructor
    public Session(String sessionID, String sessionName, Speaker speaker, Date date, String time, String room) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.speaker = speaker;
        this.date = date;
        this.time = time;
        this.room = room;
        this.attendees = new ArrayList<>();
        this.feedbackList = new ArrayList<>();
    }

    // Minimal Constructor
    public Session(String sessionID, String sessionName) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.attendees = new ArrayList<>();
        this.feedbackList = new ArrayList<>();
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

    // Register an attendee for the session
    public void registerAttendance(Attendee attendee) {
        if (!attendees.contains(attendee)) {
            attendees.add(attendee);
            attendee.addToSchedule(this);
        } else {
            System.err.println("Attendee " + attendee.getName() + " is already registered for session: " + sessionName);
        }
    }

    // Add feedback for the session
    public void addFeedback(Feedback feedback) {
        if (!feedbackList.contains(feedback)) {
            feedbackList.add(feedback);
        } else {
            System.err.println("Duplicate feedback detected for session: " + sessionName);
        }
    }

    // Get session details in a formatted string
    public String getSessionDetails() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sessionName + " with " + (speaker != null ? speaker.getName() : "Unknown Speaker") + " on "
                + (date != null ? sdf.format(date) : "Unknown Date") + " at " + (time != null ? time : "Unknown Time")
                + " in " + (room != null ? room : "Unknown Room");
    }

    // Load all sessions using SessionDatabase
    public static List<Session> loadSessions() throws Exception {
        return SessionDatabase.loadAllSessions();
    }

    // Save all sessions using SessionDatabase
    public static void saveSessions(List<Session> sessions) throws Exception {
        SessionDatabase.saveAllSessions(sessions);
    }

    // Add a new session using SessionDatabase
    public static void addSession(Session session) throws Exception {
        SessionDatabase.addSession(session);
    }
}

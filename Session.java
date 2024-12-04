import java.io.*;
import java.text.ParseException;
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

    public void registerAttendance(Attendee attendee) {
        attendees.add(attendee);
        attendee.addToSchedule(this);
    }
    
    public void addFeedback(Feedback feedback) {
        feedbackList.add(feedback);
    }
    
    public String getSessionDetails() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sessionName + " with " + speaker.getName() + " on " + sdf.format(date) + " at " + time + " in " + room;
    }

    public static List<Session> loadSessions() throws Exception {
        return SessionDatabase.loadSessions();
    }

    public static void saveSessions(List<Session> sessions) throws Exception {
        SessionDatabase.saveSessions(sessions);
    }

    public static void addSession(Session session) throws Exception {
        SessionDatabase.addSession(session);
    }
}

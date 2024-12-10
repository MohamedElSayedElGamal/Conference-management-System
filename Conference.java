import java.util.*;
import java.text.*;

public class Conference {
    private String name;
    private Date startDate;
    private Date endDate;
    private List<Session> sessions;
    private List<Attendee> attendees;
    private List<Feedback> feedbackList;

    public Conference(String name, Date startDate, Date endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessions = new ArrayList<>();
        this.attendees = new ArrayList<>();
        this.feedbackList = new ArrayList<>();
    }

    public String getConferanceName() {
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

    public void openSession(Session session) {
        sessions.add(session);
    }

    public void registerAttendee(Attendee attendee) {
        attendees.add(attendee);
    }

    public static List<Conference> loadConferences() throws Exception {
        return ConferenceDatabase.loadConferences();
    }

    public static void saveConferences(List<Conference> conferences) throws Exception {
        ConferenceDatabase.saveConferences(conferences);
    }

    public static void addConference(Conference conference) throws Exception {
        ConferenceDatabase.addConference(conference);
    }
    

    public void addFeedback(Feedback feedback) {
        this.feedbackList.add(feedback);
    }
}

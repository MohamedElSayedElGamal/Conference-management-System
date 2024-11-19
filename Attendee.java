import java.util.ArrayList;
import java.util.List;

public class Attendee {
    private String attendeeID;
    private String name;
    private String email;
    private List<Feedback> feedbackList;

    public Attendee(String attendeeID, String name, String email) {
        this.attendeeID = attendeeID;
        this.name = name;
        this.email = email;
        this.feedbackList = new ArrayList<>();
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

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void submitFeedback(Feedback feedback) {
        feedbackList.add(feedback);
    }
}

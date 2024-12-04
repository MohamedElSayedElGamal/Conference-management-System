import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feedback {
    private String feedbackID;
    private String attendeeID;
    private String sessionID;
    private String comment;
    private int rating;

    public Feedback(String feedbackID, String attendeeID, String sessionID, String comment, int rating) {
        this.feedbackID = feedbackID;
        this.attendeeID = attendeeID;
        this.sessionID = sessionID;
        this.comment = comment;
        this.rating = rating;
    }

    public String getFeedbackID() {
        return feedbackID;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public static List<Feedback> loadFeedback() throws Exception {
        return FeedbackDatabase.loadFeedback();
    }

    public static void saveFeedback(List<Feedback> feedbackList) throws Exception {
        FeedbackDatabase.saveFeedback(feedbackList);
    }

    public static void addFeedback(Feedback feedback) throws Exception {
        FeedbackDatabase.addFeedback(feedback);
    }
}

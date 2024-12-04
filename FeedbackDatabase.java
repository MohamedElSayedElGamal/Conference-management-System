import java.io.*;
import java.util.*;

public class FeedbackDatabase {

    private static final String FEEDBACK_FILE = "feedback.csv";

    public static List<Feedback> loadFeedback() throws IOException {
        List<Feedback> feedbackList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String feedbackID = row[0];
                String attendeeID = row[1];
                String sessionID = row[2];
                String comment = row[3];
                int rating = Integer.parseInt(row[4]);
                Feedback feedback = new Feedback(feedbackID, attendeeID, sessionID, comment, rating);
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }

    public static void saveFeedback(List<Feedback> feedbackList) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEEDBACK_FILE))) {
            for (Feedback feedback : feedbackList) {
                bw.write(feedback.getFeedbackID() + "," + feedback.getAttendeeID() + "," + feedback.getSessionID()
                        + "," + feedback.getComment() + "," + feedback.getRating());
                bw.newLine();
            }
        }
    }

    public static void addFeedback(Feedback feedback) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEEDBACK_FILE, true))) {
            bw.write(feedback.getFeedbackID() + "," + feedback.getAttendeeID() + "," + feedback.getSessionID()
                    + "," + feedback.getComment() + "," + feedback.getRating());
            bw.newLine();
        }
    }
}

import java.io.*;
import java.util.*;

public class FeedbackDatabase {

    // Path to the feedback CSV file
    private static final String FEEDBACK_FILE = "C:\\New folder\\Java_project\\feedback.csv";

    // Add a single feedback entry to the CSV file
    public static void addFeedback(Feedback feedback) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEEDBACK_FILE, true))) {
            // Append the new feedback entry to the file
            bw.write(feedback.getAttendeeID() + "," + feedback.getComment() + "," + feedback.getRating());
            bw.newLine();
        }
    }
}

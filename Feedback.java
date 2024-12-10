import java.io.*;
import java.util.*;

public class Feedback {
    private String attendeeID;
    private String comment;
    private int rating;

    // Constructor without session ID
    public Feedback(String attendeeID, String comment, int rating) {
        this.attendeeID = attendeeID;
        this.comment = comment;
        this.rating = rating;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    // Path to the feedback CSV file
    private static final String FEEDBACK_FILE = "D:\\Java_project\\feedback.csv";

    // Add a new feedback entry to the CSV file
    public static void addFeedback(Feedback feedback) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEEDBACK_FILE, true))) {
            // Append the new feedback entry to the file without session ID
            bw.write(feedback.getAttendeeID() + "," 
                    + feedback.getComment() + "," + feedback.getRating());
            bw.newLine();
        }
    }
}

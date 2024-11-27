import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Attendee {
    private String attendeeID;
    private String name;
    private String email;
    private String feedbackComment;
    private Integer feedbackRating; // Use Integer to allow null for no feedback.

    public Attendee(String attendeeID, String name, String email, String feedbackComment, Integer feedbackRating) {
        this.attendeeID = attendeeID;
        this.name = name;
        this.email = email;
        this.feedbackComment = feedbackComment;
        this.feedbackRating = feedbackRating;
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

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public Integer getFeedbackRating() {
        return feedbackRating;
    }

    public void submitFeedback(String comment, int rating) {
        this.feedbackComment = comment;
        this.feedbackRating = rating;
    }

    @Override
    public String toString() {
        return attendeeID + "," + name + "," + email + "," +
                (feedbackComment != null ? feedbackComment : "") + "," +
                (feedbackRating != null ? feedbackRating : "");
    }

    // Load attendees from a single CSV file
    public static List<Attendee> loadFromCSV(String filePath) {
        List<Attendee> attendees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) { // Ensure minimum data for attendee
                    String id = parts[0];
                    String name = parts[1];
                    String email = parts[2];
                    String comment = parts.length > 3 ? parts[3] : null;
                    Integer rating = (parts.length > 4 && !parts[4].isEmpty()) ? Integer.parseInt(parts[4]) : null;
                    attendees.add(new Attendee(id, name, email, comment, rating));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading attendees: " + e.getMessage());
        }
        return attendees;
    }

    // Save attendees to a single CSV file
    public static void saveToCSV(String filePath, List<Attendee> attendees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Attendee attendee : attendees) {
                bw.write(attendee.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving attendees: " + e.getMessage());
        }
    }
}

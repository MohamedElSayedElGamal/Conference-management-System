import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String CSV_FILE = "attendees.csv";

    // Save attendees to the CSV file
    public static void saveAttendees(List<Attendee> attendees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("AttendeeID,Name,Email,Comment,Rating\n"); // Header
            for (Attendee attendee : attendees) {
                for (Feedback feedback : attendee.getFeedbacks()) {
                    writer.write(attendee.getAttendeeID() + "," +
                                 attendee.getName() + "," +
                                 attendee.getEmail() + "," +
                                 feedback.getComment() + "," +
                                 feedback.getRating() + "\n");
                }
                if (attendee.getFeedbacks().isEmpty()) {
                    writer.write(attendee.getAttendeeID() + "," +
                                 attendee.getName() + "," +
                                 attendee.getEmail() + ",,\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load attendees from the CSV file
    public static List<Attendee> loadAttendees() {
        List<Attendee> attendees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Use -1 to include empty fields
                String attendeeID = parts[0];
                String name = parts[1];
                String email = parts[2];
                String comment = parts[3];
                int rating = parts[4].isEmpty() ? -1 : Integer.parseInt(parts[4]); // -1 if no rating

                // Check if attendee already exists
                Attendee attendee = attendees.stream()
                        .filter(a -> a.getAttendeeID().equals(attendeeID))
                        .findFirst()
                        .orElse(null);

                if (attendee == null) {
                    attendee = new Attendee(attendeeID, name, email);
                    attendees.add(attendee);
                }

                // Add feedback if available
                if (!comment.isEmpty()) {
                    Feedback feedback = new Feedback(attendeeID, comment, rating);
                    attendee.submitFeedback(feedback);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("CSV file not found. Creating a new one...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attendees;
    }
}

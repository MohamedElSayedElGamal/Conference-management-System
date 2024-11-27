import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:conference.db";

    public DatabaseManager() {
        initializeDatabase();
    }

    // Initialize database and create tables if they don't exist
    private void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Create Attendees table
            String createAttendeesTable = """
                    CREATE TABLE IF NOT EXISTS Attendees (
                        attendeeID TEXT PRIMARY KEY,
                        name TEXT NOT NULL,
                        email TEXT NOT NULL
                    );
                    """;
            stmt.execute(createAttendeesTable);

            // Create Feedback table
            String createFeedbackTable = """
                    CREATE TABLE IF NOT EXISTS Feedback (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        attendeeID TEXT NOT NULL,
                        comment TEXT,
                        rating INTEGER,
                        FOREIGN KEY (attendeeID) REFERENCES Attendees(attendeeID)
                    );
                    """;
            stmt.execute(createFeedbackTable);

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    // Connect to the database
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    // Add an attendee
    public void addAttendee(Attendee attendee) {
        String sql = "INSERT INTO Attendees (attendeeID, name, email) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attendee.getAttendeeID());
            pstmt.setString(2, attendee.getName());
            pstmt.setString(3, attendee.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding attendee: " + e.getMessage());
        }
    }

    // Retrieve all attendees
    public List<Attendee> getAllAttendees() {
        List<Attendee> attendees = new ArrayList<>();
        String sql = "SELECT * FROM Attendees";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                attendees.add(new Attendee(
                        rs.getString("attendeeID"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving attendees: " + e.getMessage());
        }
        return attendees;
    }

    // Add feedback
    public void addFeedback(Feedback feedback) {
        String sql = "INSERT INTO Feedback (attendeeID, comment, rating) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, feedback.getAttendeeID());
            pstmt.setString(2, feedback.getComment());
            pstmt.setInt(3, feedback.getRating());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding feedback: " + e.getMessage());
        }
    }

    // Retrieve feedback for an attendee
    public List<Feedback> getFeedbackForAttendee(String attendeeID) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM Feedback WHERE attendeeID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attendeeID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                feedbackList.add(new Feedback(
                        rs.getString("attendeeID"),
                        rs.getString("comment"),
                        rs.getInt("rating")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving feedback: " + e.getMessage());
        }
        return feedbackList;
    }
}

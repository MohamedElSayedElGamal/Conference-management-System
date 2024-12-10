import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Attendee {
    private String attendeeID;
    private String name;
    private String email;
    private List<Session> schedule;              // List to store actual Session objects (if needed)
    private List<String> registeredSessions;     // List to store session IDs (for CSV saving)

    // Constructor for Attendee
    public Attendee(String attendeeID, String name, String email) {
        this.attendeeID = attendeeID;
        this.name = name;
        this.email = email;
        this.schedule = new ArrayList<>();
        this.registeredSessions = new ArrayList<>();
    }

    // Getters and setters
    public String getAttendeeID() {
        return attendeeID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Session> getSchedule() {
        return schedule;
    }

    public static List<Session> getSessionsForAttendee(String attendeeID) throws IOException {
    // Load the attendee by their ID
    Attendee attendee = loadAttendeeByID(attendeeID);
    if (attendee != null) {
        // Return the list of sessions associated with the attendee
        return attendee.getSchedule(); // Assuming 'getSchedule' returns a list of sessions
    }
    return Collections.emptyList(); // Return an empty list if the attendee is not found
}


    // Add session to schedule and ensure the session ID is added to registeredSessions
    public void addToSchedule(Session session) {
        for (Session s : schedule) {
            if (s.getSessionID().equals(session.getSessionID())) {
                // Session already in the schedule
                return;
            }
        }
        schedule.add(session);
    }


    // Remove session from schedule and ensure the session ID is removed from registeredSessions
    public void removeFromSchedule(Session session) {
        if (schedule.remove(session)) {
            registeredSessions.remove(session.getSessionID());  // Remove the session ID from registeredSessions
        }
    }

    // Generate a certificate of attendance for the attendee
    public String generateCertificate() {
        return "Certificate of Attendance for " + name + " for attending " + schedule.size() + " sessions.";
    }

    // Register an attendee for a session by its ID (added to both schedule and registeredSessions)
    public void registerForSession(String sessionID) throws IOException {
        // Use SessionDatabase to load the session by ID
        Session session = SessionDatabase.loadSessionByID(sessionID);

        if (session != null && !registeredSessions.contains(sessionID)) {
            addToSchedule(session);  // Add session to attendee's schedule
            addSession(sessionID);   // Add session ID to registeredSessions list
        } else if (registeredSessions.contains(sessionID)) {
            System.out.println("Attendee is already registered for session " + sessionID);
        } else {
            System.out.println("Session with ID " + sessionID + " not found.");
        }
    }

    // Adds session ID to registeredSessions if not already added
    public void addSession(String sessionID) {
        if (!registeredSessions.contains(sessionID)) {
            registeredSessions.add(sessionID);
        }
    }

    // Converts the list of registered session IDs into a semicolon-separated string
    public String getRegisteredSessionsAsString() {
        return schedule.stream()
                .map(session -> session.getSessionID() + ":" + session.getSessionName()) // Include session ID and name
                .collect(Collectors.joining(";")); // Format: "ID:Name;ID:Name"
    }
    
    // Add toAttendee method to load sessions properly
    public static List<Attendee> loadAttendees() throws Exception {
        List<Attendee> attendees = AttendeeDatabase.loadAttendees();
        for (Attendee attendee : attendees) {
            // Parse the session data and register the sessions for each attendee
            String sessionData = attendee.getSessionData(); // Retrieve the session data from CSV
            attendee.setSessionData(sessionData); // Set session data by parsing and adding to schedule
        }
        return attendees;
    }
    
    // Modified toCSVString to handle session data properly
    public String toCSVString() {
        String sessionDetails = getSessionData();  // Get formatted session details
        return attendeeID + "," + name + "," + email + "," + (sessionDetails.isEmpty() ? "" : sessionDetails);
    }


    // Static method to add a new attendee to the database
    public static void addAttendee(Attendee attendee) throws Exception {
        AttendeeDatabase.addAttendee(attendee);
    }

    // Static method to load an attendee by their ID
    public static Attendee loadAttendeeByID(String attendeeID) throws IOException {
        return AttendeeDatabase.loadAttendeeByID(attendeeID);
    }

    // Load attendee from the database by ID and display the data in a GUI (e.g., for displaying on login)

    // Method to load and display attendee data based on ID
    public static Attendee loadAttendeeByID(String attendeeID, Conference conference) {
    try {
        // Attempt to load the attendee from the database
        Attendee attendee = AttendeeDatabase.loadAttendeeByID(attendeeID);

        if (attendee != null) {
            // If the attendee is found, display their information in the console
            System.out.println("Attendee Info:");
            System.out.println("ID: " + attendee.getAttendeeID());
            System.out.println("Name: " + attendee.getName());
            System.out.println("Email: " + attendee.getEmail());
            System.out.println("Registered Sessions: " + attendee.getRegisteredSessionsAsString());
        
            // Open the Attendee GUI and pass the attendee and conference data
            AttendeeGUI attendeeGUI = new AttendeeGUI(conference);
            attendeeGUI.setVisible(true);

        } else {
            System.out.println("No attendee found with ID: " + attendeeID);
        }

        return attendee;
    } catch (Exception e) {
        // Handle IOExceptions
        System.err.println("Error loading attendee data: " + e.getMessage());
        return null;
    }
    
}


public String getSessionData() {
        StringBuilder sessionData = new StringBuilder();
        for (Session session : schedule) {
            sessionData.append(session.getSessionID()).append(":")
                       .append(session.getSessionName()).append(";");
        }
        // Remove the last semicolon
        if (sessionData.length() > 0) {
            sessionData.setLength(sessionData.length() - 1);
        }
        return sessionData.toString();
    }

    // Set session data as a single string (e.g., "sessionID:sessionName;")
    public void setSessionData(String sessionData) {
        this.schedule.clear(); // Clear any existing schedule
        String[] sessions = sessionData.split(";");
        for (String sessionStr : sessions) {
            String[] parts = sessionStr.split(":");
            if (parts.length == 2) {
                String sessionID = parts[0].trim();
                String sessionName = parts[1].trim();
                this.schedule.add(new Session(sessionID, sessionName));
            }
        }
    }
    
    

}


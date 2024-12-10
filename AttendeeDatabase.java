import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;              // Swing components for GUI
import javax.swing.table.DefaultTableModel;  // For creating a table model
import java.awt.*;                  // For layout management
import java.awt.event.ActionListener; // For handling actions (e.g., button clicks)
import java.io.*;                   // For file input/output (BufferedReader, FileReader, etc.)
import java.text.ParseException;    // For handling date parsing exceptions
import java.text.SimpleDateFormat;  // For formatting and parsing dates
import java.util.Date;              // For using Date objects
import java.util.List;              // For using List data structures
import java.util.ArrayList;         // For using ArrayList data structure
import java.util.Map;               // For using Map data structures
import java.util.HashMap;           // For creating a HashMap (key-value pairs)
import java.util.Collections;       // For using utility methods like emptyList()


public class AttendeeDatabase {
    private static final String ATTENDEE_FILE = "C:\\New folder\\Java_project\\attendees.csv";
    private static final Map<String, Attendee> attendeeMap = new HashMap<>(); // Cached map for fast lookup

    // Static block to initialize the map when the class is loaded
    static {
        try {
            // Initialize the map with attendees from the CSV
            List<Attendee> attendees = loadAttendees();
            for (Attendee attendee : attendees) {
                attendeeMap.put(attendee.getAttendeeID(), attendee);
            }
        } catch (IOException e) {
            System.err.println("Error initializing attendee map: " + e.getMessage());
        }
    }

    // Load all attendees from the CSV file
    public static List<Attendee> loadAttendees() throws IOException {
        List<Attendee> attendeeList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDEE_FILE))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length < 3) continue;  // Skip rows with incomplete data

                String attendeeID = row[0].trim();
                String name = row[1].trim();
                String email = row[2].trim();
                Attendee attendee = new Attendee(attendeeID, name, email);

                // Handle sessions if present
                if (row.length > 3 && row[3].trim().length() > 0) {
                    String sessionData = row[3].trim();
                    attendee.setSessionData(sessionData);  // Set sessions by parsing the CSV string
                }

                attendeeList.add(attendee);
            }
        } catch (IOException e) {
            System.err.println("Error reading attendees: " + e.getMessage());
        }
        return attendeeList;
    }

    // Get the list of sessions registered for an attendee
    

    // Load an attendee by their ID
    public static Attendee loadAttendeeByID(String attendeeID) {
        return attendeeMap.get(attendeeID);
    }

    // Add a new attendee to the CSV file
    public static void addAttendee(Attendee attendee) throws IOException {
        if (attendeeMap.containsKey(attendee.getAttendeeID())) {
            throw new IllegalArgumentException("Attendee with ID " + attendee.getAttendeeID() + " already exists.");
        }

        // Add to the map and save to the CSV
        attendeeMap.put(attendee.getAttendeeID(), attendee);
        saveAttendees(new ArrayList<>(attendeeMap.values()));
    }

    // Save all attendees to the CSV file
    public static void saveAttendees(List<Attendee> attendees) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ATTENDEE_FILE))) {
            bw.write("ID,Name,Email,Sessions\n");  // Write header
            for (Attendee attendee : attendees) {
                bw.write(attendee.toCSVString());  // Save each attendee to CSV
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving attendees: " + e.getMessage());
        }
    }
}

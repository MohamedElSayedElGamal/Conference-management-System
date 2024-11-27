import java.util.ArrayList;
import java.util.List;

public class ConferenceManagementSystem {
    public static void main(String[] args) {
        List<Attendee> attendees = DatabaseHandler.loadAttendees(); // Load from CSV
        new AttendeeGUI(attendees);
        new ManagementGUI(attendees);
    }
}

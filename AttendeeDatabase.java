import java.io.*;
import java.util.*;

public class AttendeeDatabase {

    private static final String ATTENDEE_FILE = "attendees.csv";

    public static List<Attendee> loadAttendees() throws IOException {
        List<Attendee> attendeeList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDEE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String attendeeID = row[0];
                String name = row[1];
                String email = row[2];
                Attendee attendee = new Attendee(attendeeID, name, email);
                attendeeList.add(attendee);
            }
        }
        return attendeeList;
    }

    public static void saveAttendees(List<Attendee> attendees) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ATTENDEE_FILE))) {
            for (Attendee attendee : attendees) {
                bw.write(attendee.getAttendeeID() + "," + attendee.getName() + "," + attendee.getEmail());
                bw.newLine();
            }
        }
    }

    public static void addAttendee(Attendee attendee) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ATTENDEE_FILE, true))) {
            bw.write(attendee.getAttendeeID() + "," + attendee.getName() + "," + attendee.getEmail());
            bw.newLine();
        }
    }
}

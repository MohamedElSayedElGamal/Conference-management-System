import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManagementGUI {
    private JFrame frame;
    private JTextArea displayArea;

    private List<Attendee> attendees;

    public ManagementGUI(List<Attendee> attendees) {
        this.attendees = attendees;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Management Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshAttendeeList());

        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        frame.add(refreshButton, BorderLayout.SOUTH);

        refreshAttendeeList(); // Load data initially
        frame.setVisible(true);
    }

    private void refreshAttendeeList() {
        displayArea.setText("Attendees:\n");
        for (Attendee attendee : attendees) {
            displayArea.append("ID: " + attendee.getAttendeeID() + ", Name: " + attendee.getName() + ", Email: " + attendee.getEmail() + "\n");
            for (Feedback feedback : attendee.getFeedbacks()) {
                displayArea.append("  Feedback - Comment: " + feedback.getComment() + ", Rating: " + feedback.getRating() + "\n");
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AttendeeGUI {
    private JFrame frame;
    private JTextField attendeeIDField;
    private JTextField attendeeNameField;
    private JTextField attendeeEmailField;

    private List<Attendee> attendees;

    public AttendeeGUI(List<Attendee> attendees) {
        this.attendees = attendees;
        initialize();
    }

    private void initialize() {
        attendees.addAll(DatabaseHandler.loadAttendees()); // Load data from CSV

        frame = new JFrame("Attendee Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 2, 5, 5));

        JLabel attendeeIDLabel = new JLabel("Attendee ID:");
        attendeeIDField = new JTextField();
        JLabel attendeeNameLabel = new JLabel("Name:");
        attendeeNameField = new JTextField();
        JLabel attendeeEmailLabel = new JLabel("Email:");
        attendeeEmailField = new JTextField();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAction());

        frame.add(attendeeIDLabel);
        frame.add(attendeeIDField);
        frame.add(attendeeNameLabel);
        frame.add(attendeeNameField);
        frame.add(attendeeEmailLabel);
        frame.add(attendeeEmailField);
        frame.add(new JLabel()); // Empty space
        frame.add(registerButton);

        frame.setVisible(true);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String attendeeID = attendeeIDField.getText();
            String name = attendeeNameField.getText();
            String email = attendeeEmailField.getText();

            if (attendeeID.isEmpty() || name.isEmpty() || email.isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Ensure all fields are filled and email is valid.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Attendee attendee = new Attendee(attendeeID, name, email);
            attendees.add(attendee);
            DatabaseHandler.saveAttendees(attendees); // Save data to CSV

            JOptionPane.showMessageDialog(frame, "Attendee registered successfully!");
            attendeeIDField.setText("");
            attendeeNameField.setText("");
            attendeeEmailField.setText("");
        }
    }
}

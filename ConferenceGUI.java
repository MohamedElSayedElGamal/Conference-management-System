import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ConferenceGUI {
    private JFrame frame;
    private JTextField attendeeIDField;
    private JTextField attendeeNameField;
    private JTextField attendeeEmailField;
    private JTextField feedbackAttendeeIDField;
    private JTextArea feedbackCommentField;
    private JTextField feedbackRatingField;
    private JTextArea displayArea;

    private List<Attendee> attendees;

    public ConferenceGUI() {
        attendees = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        // Frame setup
        frame = new JFrame("Conference Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Panel for attendee registration
        JPanel attendeePanel = new JPanel();
        attendeePanel.setLayout(new GridLayout(4, 2, 5, 5));
        attendeePanel.setBorder(BorderFactory.createTitledBorder("Register Attendee"));

        JLabel attendeeIDLabel = new JLabel("Attendee ID:");
        attendeeIDField = new JTextField();
        JLabel attendeeNameLabel = new JLabel("Name:");
        attendeeNameField = new JTextField();
        JLabel attendeeEmailLabel = new JLabel("Email:");
        attendeeEmailField = new JTextField();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAttendeeAction());

        attendeePanel.add(attendeeIDLabel);
        attendeePanel.add(attendeeIDField);
        attendeePanel.add(attendeeNameLabel);
        attendeePanel.add(attendeeNameField);
        attendeePanel.add(attendeeEmailLabel);
        attendeePanel.add(attendeeEmailField);
        attendeePanel.add(new JLabel()); // Empty space
        attendeePanel.add(registerButton);

        // Panel for feedback
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new GridLayout(4, 2, 5, 5));
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Submit Feedback"));

        JLabel feedbackAttendeeIDLabel = new JLabel("Attendee ID:");
        feedbackAttendeeIDField = new JTextField();
        JLabel feedbackCommentLabel = new JLabel("Comment:");
        feedbackCommentField = new JTextArea(2, 20);
        JLabel feedbackRatingLabel = new JLabel("Rating (1-5):");
        feedbackRatingField = new JTextField();

        JButton submitFeedbackButton = new JButton("Submit Feedback");
        submitFeedbackButton.addActionListener(new SubmitFeedbackAction());

        feedbackPanel.add(feedbackAttendeeIDLabel);
        feedbackPanel.add(feedbackAttendeeIDField);
        feedbackPanel.add(feedbackCommentLabel);
        feedbackPanel.add(new JScrollPane(feedbackCommentField));
        feedbackPanel.add(feedbackRatingLabel);
        feedbackPanel.add(feedbackRatingField);
        feedbackPanel.add(new JLabel()); // Empty space
        feedbackPanel.add(submitFeedbackButton);

        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Display Area"));

        // Add panels to the frame
        frame.add(attendeePanel, BorderLayout.NORTH);
        frame.add(feedbackPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // ActionListener for registering attendees
    private class RegisterAttendeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String attendeeID = attendeeIDField.getText();
            String name = attendeeNameField.getText();
            String email = attendeeEmailField.getText();

            if (attendeeID.isEmpty() || name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(frame, "Invalid email! Must contain '@'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Attendee attendee = new Attendee(attendeeID, name, email);
            attendees.add(attendee);

            displayArea.append("Attendee registered: " + name + " (" + attendeeID + ")\n");

            // Clear fields
            attendeeIDField.setText("");
            attendeeNameField.setText("");
            attendeeEmailField.setText("");
        }
    }

    // ActionListener for submitting feedback
    private class SubmitFeedbackAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String attendeeID = feedbackAttendeeIDField.getText();
            String comment = feedbackCommentField.getText();
            String ratingText = feedbackRatingField.getText();

            if (attendeeID.isEmpty() || comment.isEmpty() || ratingText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find attendee by ID
            Attendee attendee = null;
            for (Attendee a : attendees) {
                if (a.getAttendeeID().equals(attendeeID)) {
                    attendee = a;
                    break;
                }
            }

            if (attendee == null) {
                JOptionPane.showMessageDialog(frame, "Attendee with the given ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rating;
            try {
                rating = Integer.parseInt(ratingText);
                if (rating < 1 || rating > 5) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Rating must be an integer between 1 and 5!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Feedback feedback = new Feedback(attendeeID, comment, rating);
            attendee.submitFeedback(feedback);

            displayArea.append("Feedback submitted for attendee: " + attendee.getName() + "\n");

            // Clear fields
            feedbackAttendeeIDField.setText("");
            feedbackCommentField.setText("");
            feedbackRatingField.setText("");
        }
    }

    public static void main(String[] args) {
        new ConferenceGUI();
    }
}

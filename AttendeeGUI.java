import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AttendeeGUI extends JFrame {
    private Conference conference;
    private JTextField attendeeIDField, nameField, emailField;
    private JTable sessionTable, attendeeScheduleTable, feedbackTable;
    private DefaultTableModel sessionTableModel, attendeeScheduleTableModel, feedbackTableModel;

    public AttendeeGUI(Conference conference) {
        this.conference = conference;
        initUI();
    }

    private void initUI() {
        setTitle("Attendee Portal");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create Input Fields and Labels
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Attendee ID:"));
        attendeeIDField = new JTextField();
        inputPanel.add(attendeeIDField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        // Create Register Button
        JButton registerButton = new JButton("Register for Session");
        registerButton.addActionListener(e -> registerForSession());

        // Create View Sessions Button
        JButton viewSessionsButton = new JButton("View Sessions");
        viewSessionsButton.addActionListener(e -> viewSessions());

        // Create View Schedule Button
        JButton viewScheduleButton = new JButton("View Schedule");
        viewScheduleButton.addActionListener(e -> viewSchedule());

        // Create Submit Feedback Button
        JButton submitFeedbackButton = new JButton("Submit Feedback");
        submitFeedbackButton.addActionListener(e -> submitFeedback());

        // Layout Panel for Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(viewSessionsButton);
        buttonPanel.add(viewScheduleButton);
        buttonPanel.add(submitFeedbackButton);

        // Table for displaying sessions
        sessionTableModel = new DefaultTableModel(new String[] {"Session ID", "Session Name", "Speaker", "Date", "Time", "Room"}, 0);
        sessionTable = new JTable(sessionTableModel);
        JScrollPane sessionScrollPane = new JScrollPane(sessionTable);

        // Table for displaying attendee's schedule
        attendeeScheduleTableModel = new DefaultTableModel(new String[] {"Session ID", "Session Name"}, 0);
        attendeeScheduleTable = new JTable(attendeeScheduleTableModel);
        JScrollPane attendeeScheduleScrollPane = new JScrollPane(attendeeScheduleTable);

        // Table for displaying feedback
        feedbackTableModel = new DefaultTableModel(new String[] {"Session ID", "Feedback", "Rating"}, 0);
        feedbackTable = new JTable(feedbackTableModel);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackTable);

        // Layout all components properly
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 3));
        centerPanel.add(sessionScrollPane);
        centerPanel.add(attendeeScheduleScrollPane);
        centerPanel.add(feedbackScrollPane);

        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void registerForSession() {
        String attendeeID = attendeeIDField.getText();
        String name = nameField.getText();
        String email = emailField.getText();

        if (attendeeID.isEmpty() || name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // Check if the attendee already exists
        Attendee attendee = null;
        for (Attendee existingAttendee : conference.getAttendees()) {
            if (existingAttendee.getAttendeeID().equals(attendeeID)) {
                attendee = existingAttendee;
                break;
            }
        }

        if (attendee == null) {
            // Create a new Attendee if they don't exist
            attendee = new Attendee(attendeeID, name, email);
            conference.registerAttendee(attendee);
        }

        // Find available sessions and let the user register
        String sessionID = JOptionPane.showInputDialog(this, "Enter Session ID to Register for:");

        boolean sessionFound = false;
        for (Session session : conference.getSessions()) {
            if (session.getSessionID().equals(sessionID)) {
                session.registerAttendance(attendee);
                JOptionPane.showMessageDialog(this, "Registered for session: " + session.getSessionName());
                sessionFound = true;
                break;
            }
        }

        if (!sessionFound) {
            JOptionPane.showMessageDialog(this, "Session not found.");
        }
    }

    private void viewSessions() {
        // Clear current session table
        sessionTableModel.setRowCount(0);

        // Fetch and display sessions in table
        List<Session> sessions = conference.getSessions();
        if (sessions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No sessions available.");
            return;
        }

        for (Session session : sessions) {
            sessionTableModel.addRow(new Object[] {
                session.getSessionID(),
                session.getSessionName(),
                session.getSpeaker().getName(),
                session.getDate(),
                session.getTime(),
                session.getRoom()
            });
        }
    }

    private void viewSchedule() {
        // Clear current schedule table
        attendeeScheduleTableModel.setRowCount(0);

        // Fetch and display attendee's schedule in table
        String attendeeID = attendeeIDField.getText();
        Attendee attendee = null;
        for (Attendee existingAttendee : conference.getAttendees()) {
            if (existingAttendee.getAttendeeID().equals(attendeeID)) {
                attendee = existingAttendee;
                break;
            }
        }

        if (attendee == null) {
            JOptionPane.showMessageDialog(this, "Attendee not found.");
            return;
        }

        for (Session session : attendee.getSchedule()) {
            attendeeScheduleTableModel.addRow(new Object[] {
                session.getSessionID(),
                session.getSessionName()
            });
        }
    }

    private void submitFeedback() {
    String attendeeID = attendeeIDField.getText();
    String sessionID = JOptionPane.showInputDialog(this, "Enter Session ID to Provide Feedback:");

    if (attendeeID.isEmpty() || sessionID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Attendee ID and Session ID are required.");
        return;
    }

    Attendee attendee = null;
    for (Attendee existingAttendee : conference.getAttendees()) {
        if (existingAttendee.getAttendeeID().equals(attendeeID)) {
            attendee = existingAttendee;
            break;
        }
    }

    if (attendee == null) {
        JOptionPane.showMessageDialog(this, "Attendee not found.");
        return;
    }

    Session session = null;
    for (Session s : conference.getSessions()) {
        if (s.getSessionID().equals(sessionID)) {
            session = s;
            break;
        }
    }

    if (session == null) {
        JOptionPane.showMessageDialog(this, "Session not found.");
        return;
    }

    String comment = JOptionPane.showInputDialog(this, "Enter feedback:");

    if (comment.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Feedback cannot be empty.");
        return;
    }

    int rating;
    try {
        rating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Rating (1-5):"));
        if (rating < 1 || rating > 5) {
            JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a number between 1 and 5.");
        return;
    }

    // Generate a unique feedback ID
    String feedbackID = "FB-" + System.currentTimeMillis();

    // Create feedback object using the correct constructor
    Feedback feedback = new Feedback(feedbackID, attendeeID, sessionID, comment, rating);

    // Add feedback to session
    session.addFeedback(feedback);
    JOptionPane.showMessageDialog(this, "Feedback submitted successfully.");
}


public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        // Create a sample conference for the GUI
        Conference conference = new Conference("Tech Summit", new Date(), new Date());
        AttendeeGUI attendeeGUI = new AttendeeGUI(conference);
        attendeeGUI.setVisible(true);
    });
}
}

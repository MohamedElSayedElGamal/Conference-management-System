import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class AttendeeGUI extends JFrame {
    private Conference conference;
    private Attendee attendee; // Current attendee
    private JTextField attendeeIDField, nameField, emailField, sessionIDField; // Added sessionIDField
    private JTable sessionTable, attendeeScheduleTable;
    private DefaultTableModel sessionTableModel, attendeeScheduleTableModel;

    public AttendeeGUI(Conference conference) {
        this.conference = conference;
        initUI();
    }

    private void initUI() {
    setTitle("Attendee Portal");
    setSize(1200, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Input Panel for Attendee Information
    JPanel inputPanel = new JPanel(new GridLayout(5, 2)); // Increased to 5 rows for session ID input
    inputPanel.add(new JLabel("Attendee ID:"));
    attendeeIDField = new JTextField();
    inputPanel.add(attendeeIDField);

    inputPanel.add(new JLabel("Name:"));
    nameField = new JTextField();
    inputPanel.add(nameField);

    inputPanel.add(new JLabel("Email:"));
    emailField = new JTextField();
    inputPanel.add(emailField);

    // Added Session ID input for registration
    inputPanel.add(new JLabel("Session ID:"));
    sessionIDField = new JTextField();
    inputPanel.add(sessionIDField);

    JButton saveAttendeeButton = createButton("Save Attendee", e -> saveAttendee());
    inputPanel.add(saveAttendeeButton);

    // Buttons
    JButton registerButton = createButton("Register for Session", e -> registerForSession());
    JButton viewSessionsButton = createButton("View Sessions", e -> viewSessions());
    JButton viewScheduleButton = createButton("View Schedule", e -> viewSchedule());
    JButton submitFeedbackButton = createButton("Submit Feedback", e -> submitFeedback()); // New button

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(registerButton);
    buttonPanel.add(viewSessionsButton);
    buttonPanel.add(viewScheduleButton);
    buttonPanel.add(submitFeedbackButton); // Add the submit feedback button to the panel

    // Tables (Same as before)
    sessionTableModel = new DefaultTableModel(new String[]{"Session ID", "Session Name", "Speaker", "Date", "Time", "Room"}, 0);
    sessionTable = new JTable(sessionTableModel);
    JScrollPane sessionScrollPane = new JScrollPane(sessionTable);

    attendeeScheduleTableModel = new DefaultTableModel(new String[]{"Session ID", "Session Name"}, 0);
    attendeeScheduleTable = new JTable(attendeeScheduleTableModel);
    JScrollPane attendeeScheduleScrollPane = new JScrollPane(attendeeScheduleTable);

    // Layout
    setLayout(new BorderLayout());
    JPanel centerPanel = new JPanel(new GridLayout(2, 1));
    centerPanel.add(sessionScrollPane);
    centerPanel.add(attendeeScheduleScrollPane);

    add(inputPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    // Load Sessions for Display
    viewSessions();
}


    private JButton createButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private void saveAttendee() {
    String attendeeID = attendeeIDField.getText().trim();
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();

    if (attendeeID.isEmpty() || name.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required to save an attendee.");
        return;
    }
    
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        JOptionPane.showMessageDialog(this, "Invalid email format.");
        return;
    }


    attendee = new Attendee(attendeeID, name, email);
    try {
        // Add the new attendee to the database
        AttendeeDatabase.addAttendee(attendee);
        JOptionPane.showMessageDialog(this, "Attendee saved successfully.");
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, "Attendee with this ID already exists.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving attendee: " + e.getMessage());
    }
}


    private void registerForSession() {
    if (attendee == null) {
        JOptionPane.showMessageDialog(this, "Please save attendee information first.");
        return;
    }

    String sessionID = sessionIDField.getText().trim(); // Get the session ID from the text field

    if (sessionID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a valid session ID.");
        return;
    }

    try {
        // Attempt to retrieve the session from the database
        Session session = SessionDatabase.loadSessionByID(sessionID);

        if (session != null) {
            // Check if the session is already in the attendee's schedule
            if (attendee.getSchedule().contains(session)) {
                JOptionPane.showMessageDialog(this, "You are already registered for this session.");
                return;
            }

            // Add the session to the attendee's schedule
            attendee.addToSchedule(session);

            // Update the attendee's data in the database
            AttendeeDatabase.addAttendee(attendee); // Use an update method to modify attendee data

            JOptionPane.showMessageDialog(this, "Successfully registered for session: " + session.getSessionName());
            viewSchedule();  // Refresh the attendee's schedule view
        } else {
            JOptionPane.showMessageDialog(this, "Session not found.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error registering for session: " + e.getMessage());
    }
}


    private void viewSessions() {
        sessionTableModel.setRowCount(0);
        try {
            List<Session> sessions = SessionDatabase.loadAllSessions();
            for (Session session : sessions) {
                sessionTableModel.addRow(new Object[]{
                        session.getSessionID(),
                        session.getSessionName(),
                        session.getSpeaker().getName(),
                        new SimpleDateFormat("MM/dd/yyyy").format(session.getDate()),
                        session.getTime(),
                        session.getRoom()
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading sessions: " + e.getMessage());
        }
    }

    private void viewSchedule() {
        attendeeScheduleTableModel.setRowCount(0);
        if (attendee == null) {
            JOptionPane.showMessageDialog(this, "Please save attendee information first.");
            return;
        }

        for (Session session : attendee.getSchedule()) {
            attendeeScheduleTableModel.addRow(new Object[]{session.getSessionID(), session.getSessionName()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Conference conference = new Conference("UH GAF", new Date(), new Date());
            AttendeeGUI attendeeGUI = new AttendeeGUI(conference);
            attendeeGUI.setVisible(true);
        });
    }
    
    private void submitFeedback() {
    if (attendee == null) {
        JOptionPane.showMessageDialog(this, "Please save or load attendee information first.");
        return;
    }

    // Ask for feedback comment
    String comment = JOptionPane.showInputDialog(this, "Enter feedback:");

    if (comment == null || comment.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Feedback cannot be empty.");
        return;
    }

    // Ask for rating
    int rating;
    try {
        String ratingInput = JOptionPane.showInputDialog(this, "Enter Rating (1-5):");
        rating = Integer.parseInt(ratingInput);

        if (rating < 1 || rating > 5) {
            JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a number between 1 and 5.");
        return;
    }

    // Create feedback object using the existing constructor (no need for attendeeID input)
    Feedback feedback = new Feedback(attendee.getAttendeeID(), comment, rating);

    // Save feedback to the CSV file using the FeedbackDatabase class
    try {
        FeedbackDatabase.addFeedback(feedback); // Using existing FeedbackDatabase class
        JOptionPane.showMessageDialog(this, "Feedback submitted successfully.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving feedback: " + e.getMessage());
    }
}


    
}

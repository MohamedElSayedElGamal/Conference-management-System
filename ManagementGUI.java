import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ManagementGUI extends JFrame {
    private Conference conference;
    private JTextField sessionIDField, sessionNameField, speakerNameField, speakerBioField, timeField, roomField, dateField;
    private JTable sessionTable, attendeeTable, feedbackTable;
    private DefaultTableModel sessionTableModel, attendeeTableModel, feedbackTableModel;

public ManagementGUI(Conference conference) {
    this.conference = conference;
    initUI();
}

private void initUI() {
    setTitle("Management Portal");
    setSize(1000, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Create Input Fields and Labels
    JPanel inputPanel = new JPanel(new GridLayout(8, 2));

    inputPanel.add(new JLabel("Session ID:"));
    sessionIDField = new JTextField();
    inputPanel.add(sessionIDField);

    inputPanel.add(new JLabel("Session Name:"));
    sessionNameField = new JTextField();
    inputPanel.add(sessionNameField);

    inputPanel.add(new JLabel("Speaker Name:"));
    speakerNameField = new JTextField();
    inputPanel.add(speakerNameField);

    inputPanel.add(new JLabel("Speaker Bio:"));
    speakerBioField = new JTextField();
    inputPanel.add(speakerBioField);

    inputPanel.add(new JLabel("Time:"));
    timeField = new JTextField();
    inputPanel.add(timeField);

    inputPanel.add(new JLabel("Room:"));
    roomField = new JTextField();
    inputPanel.add(roomField);

    inputPanel.add(new JLabel("Date (MM/dd/yyyy):"));
    dateField = new JTextField();
    inputPanel.add(dateField);

    // Create Add Session Button
    JButton addSessionButton = new JButton("Add Session");
    addSessionButton.addActionListener(e -> addSession());

    // Create View Sessions Button
    JButton viewSessionsButton = new JButton("View Sessions");
    viewSessionsButton.addActionListener(e -> viewSessions());

    // Create View Attendees Button
    JButton viewAttendeesButton = new JButton("View Attendees");
    viewAttendeesButton.addActionListener(e -> viewAttendees());

    // Create View Feedback Button
    JButton viewFeedbackButton = new JButton("View Feedback");
    viewFeedbackButton.addActionListener(e -> viewFeedback());

    // Layout Panel for Buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addSessionButton);
    buttonPanel.add(viewSessionsButton);
    buttonPanel.add(viewAttendeesButton);
    buttonPanel.add(viewFeedbackButton);

    // Table for displaying sessions
    sessionTableModel = new DefaultTableModel(new String[] {"Session ID", "Name", "Speaker", "Date", "Time", "Room"}, 0);
    sessionTable = new JTable(sessionTableModel);
    JScrollPane sessionScrollPane = new JScrollPane(sessionTable);

    // Table for displaying attendees
    attendeeTableModel = new DefaultTableModel(new String[] {"Attendee Name", "Email", "Session Registered"}, 0);
    attendeeTable = new JTable(attendeeTableModel);
    JScrollPane attendeeScrollPane = new JScrollPane(attendeeTable);

    // Table for displaying feedback
    feedbackTableModel = new DefaultTableModel(new String[] {"Attendee", "Feedback", "Rating"}, 0);
    feedbackTable = new JTable(feedbackTableModel);
    JScrollPane feedbackScrollPane = new JScrollPane(feedbackTable);

    // Layout all components properly
    setLayout(new BorderLayout());

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new GridLayout(1, 3));
    centerPanel.add(sessionScrollPane);
    centerPanel.add(attendeeScrollPane);
    centerPanel.add(feedbackScrollPane);

    add(inputPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
}

    private void addSession() {
    // Validate inputs
    String sessionID = sessionIDField.getText();
    String sessionName = sessionNameField.getText();
    String speakerName = speakerNameField.getText();
    String speakerBio = speakerBioField.getText();
    String time = timeField.getText();
    String room = roomField.getText();
    String dateString = dateField.getText();

    if (sessionID.isEmpty() || sessionName.isEmpty() || speakerName.isEmpty() || speakerBio.isEmpty() ||
        time.isEmpty() || room.isEmpty() || dateString.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required.");
        return;
    }

    Date date = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    dateFormat.setLenient(false);
    try {
        date = dateFormat.parse(dateString);
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Invalid date format. Please use MM/dd/yyyy.");
        return;
    }

    Speaker speaker = new Speaker(speakerName, speakerBio);
    Session session = new Session(sessionID, sessionName, speaker, date, time, room);

    // Removed the catch block
    conference.openSession(session);  // Assuming openSession method exists in the Conference class
    JOptionPane.showMessageDialog(this, "Session added successfully.");
    clearFields();
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
                new SimpleDateFormat("MM/dd/yyyy").format(session.getDate()),
                session.getTime(),
                session.getRoom()
            });
        }
    }

    private void viewAttendees() {
        // Clear current attendee table
        attendeeTableModel.setRowCount(0);

        // Fetch and display attendees in table
        List<Attendee> attendees = conference.getAttendees();
        if (attendees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No attendees available.");
            return;
        }

        for (Attendee attendee : attendees) {
            attendeeTableModel.addRow(new Object[] {
                attendee.getName(),
                attendee.getEmail(),
                attendee.getSchedule().size() > 0 ? attendee.getSchedule().get(0).getSessionName() : "Not Registered" // Handle case where attendee is not registered for a session
            });
        }
    }

    private void viewFeedback() {
        // Clear current feedback table
        feedbackTableModel.setRowCount(0);
        
        String sessionID = JOptionPane.showInputDialog(this, "Enter Session ID:");

        if (sessionID == null || sessionID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Session ID is required.");
            return;
        }
        
        // Fetch and display feedback in table
        List<Feedback> feedbackList = conference.getFeedback(sessionID);  // Get feedback list from Conference
        if (feedbackList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No feedback available.");
            return;
        }
    
        for (Feedback feedback : feedbackList) {
            feedbackTableModel.addRow(new Object[] {
                feedback.getAttendeeID(),  // Display attendee name or ID
                feedback.getComment(),
                feedback.getRating()
            });
        }
    }


    private void clearFields() {
        sessionIDField.setText("");
        sessionNameField.setText("");
        speakerNameField.setText("");
        speakerBioField.setText("");
        timeField.setText("");
        roomField.setText("");
        dateField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a sample conference for the GUI
            Conference conference = new Conference("Tech Summit", new Date(), new Date());
            ManagementGUI managementGUI = new ManagementGUI(conference);
            managementGUI.setVisible(true);
        });
    }
}

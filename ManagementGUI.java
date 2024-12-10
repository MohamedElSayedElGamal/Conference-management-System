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



public class ManagementGUI extends JFrame {
    private Conference conference;
    private JTextField sessionIDField, sessionNameField, speakerNameField, speakerBioField, timeField, roomField, dateField;
    private JTextField attendeeIDField, certificateSessionIDField;  // Declare fields for certificate
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
    JPanel inputPanel = new JPanel(new GridLayout(10, 2));
    

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
    
    inputPanel.add(new JLabel("Attendee ID (for Certificate):"));
    attendeeIDField = new JTextField();
    inputPanel.add(attendeeIDField);
    
    inputPanel.add(new JLabel("Session ID (for Certificate):"));
    certificateSessionIDField = new JTextField();
    inputPanel.add(certificateSessionIDField);
    
    
    JButton issueCertificateButton = createButton("Issue Certificate", e -> issueCertificate());  // New button
    inputPanel.add(issueCertificateButton);

    // Create Add Session Button
    JButton addSessionButton = new JButton("Add Session");
    addSessionButton.addActionListener(e -> addSession());

    // Create View Sessions Button
    JButton viewSessionsButton = new JButton("View Sessions");
    viewSessionsButton.addActionListener(e -> viewSessions());

    // Create View Attendees Button
    JButton viewAttendeesButton = new JButton("View Attendees");
    viewAttendeesButton.addActionListener(e -> viewAttendees());



    // Layout Panel for Buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addSessionButton);
    buttonPanel.add(viewSessionsButton);
    buttonPanel.add(viewAttendeesButton);
    buttonPanel.add(issueCertificateButton);
    
    // Table for displaying sessions
    sessionTableModel = new DefaultTableModel(new String[] {"Session ID", "Name", "Speaker", "Date", "Time", "Room"}, 0);
    sessionTable = new JTable(sessionTableModel);
    JScrollPane sessionScrollPane = new JScrollPane(sessionTable);

    // Table for displaying attendees
    attendeeTableModel = new DefaultTableModel(new String[] {"Attendee Name", "Email", "Session Registered"}, 0);
    attendeeTable = new JTable(attendeeTableModel);
    JScrollPane attendeeScrollPane = new JScrollPane(attendeeTable);

    // Layout all components properly
    setLayout(new BorderLayout());

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new GridLayout(1, 3));
    centerPanel.add(sessionScrollPane);
    centerPanel.add(attendeeScrollPane);


    add(inputPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Helper method to create buttons
    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
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

    // Add session to the conference
    conference.openSession(session);

    // Save the session to the database
    try {
        List<Session> allSessions = Session.loadSessions(); // Load existing sessions
        allSessions.add(session); // Add the new session to the list
        Session.saveSessions(allSessions); // Save the updated list
        JOptionPane.showMessageDialog(this, "Session added successfully and saved to the database.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saving session to the database: " + e.getMessage());
        e.printStackTrace();
        return;
    }

    // Clear the input fields
    clearFields();
    }



     private void viewSessions() {
    // Clear current session table
    sessionTableModel.setRowCount(0);

    List<Session> sessions;

    try {
        // Load sessions from the CSV file
        sessions = SessionDatabase.loadAllSessions();

        if (sessions == null || sessions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No sessions available.");
            return;
        }

        // Populate the table model with session data
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
        JOptionPane.showMessageDialog(this, "Error loading sessions from file: " + e.getMessage());
    }
    }





    private void viewAttendees() {
    // Clear current attendee table
    attendeeTableModel.setRowCount(0);

    // Fetch attendees from the AttendeeDatabase
    List<Attendee> attendees;
    try {
        attendees = AttendeeDatabase.loadAttendees();  // Load attendees from the database
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading attendees: " + e.getMessage());
        return;
    }

    // Check if the attendees list is empty
    if (attendees == null || attendees.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No attendees available.");
        return;
    }

    // Populate the attendee table with details
    for (Attendee attendee : attendees) {
        // Get the session names (or "Not Registered" if no sessions are registered)
        String sessionNames = "Not Registered";
        if (attendee.getSchedule() != null && !attendee.getSchedule().isEmpty()) {
            sessionNames = String.join(", ", attendee.getSchedule().stream()
                    .map(Session::getSessionName)  // Join session names if multiple
                        .toArray(String[]::new));
        }

        // Add the attendee details to the table
        attendeeTableModel.addRow(new Object[] {
            attendee.getName(),
            attendee.getEmail(),
            sessionNames  // Display the session names
        });
    }

    // Refresh the table to show updated data
    attendeeTableModel.fireTableDataChanged();
    }
    
    private void issueCertificate() {
    // Retrieve the attendee ID and session ID from the input fields
    String attendeeID = attendeeIDField.getText().trim();
    String sessionID = certificateSessionIDField.getText().trim();

    // Validate input fields
    if (attendeeID.isEmpty() || sessionID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Attendee ID and Session ID are required.");
        return;
    }

    // Simply show a message confirming certificate issuance
    JOptionPane.showMessageDialog(this, "Certificate issued to Attendee " + attendeeID + " for session " + sessionID);
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a sample conference for the GUI
            Conference conference = new Conference("UH-GAF", new Date(), new Date());
            ManagementGUI managementGUI = new ManagementGUI(conference);
            managementGUI.setVisible(true);
    });
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

    
}

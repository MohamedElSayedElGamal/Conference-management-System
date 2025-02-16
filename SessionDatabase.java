import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SessionDatabase {

    private static final String SESSION_FILE = "sessions.csv"; // Path to session CSV file
    private static Map<String, Session> sessionMap; // Map for fast lookup by Session ID

    static {
        try {
            // Initialize the session map
            sessionMap = new HashMap<>();
            List<Session> sessions = loadAllSessions();
            for (Session session : sessions) {
                sessionMap.put(session.getSessionID(), session);
            }
        } catch (IOException e) {
            System.err.println("Error initializing session map: " + e.getMessage());
        }
    }

    // Load sessions with full details from the CSV file
    public static List<Session> loadAllSessions() throws IOException {
        List<Session> sessionList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length < 6) {
                    System.err.println("Skipping invalid session row: " + line);
                    continue;
                }

                try {
                    String sessionID = row[0].trim();
                    String sessionName = row[1].trim();
                    String speakerName = row[2].trim();
                    Date date = dateFormat.parse(row[3].trim());
                    String time = row[4].trim();
                    String room = row[5].trim();

                    Speaker speaker = new Speaker(speakerName, "Bio"); // Placeholder bio
                    Session session = new Session(sessionID, sessionName, speaker, date, time, room);
                    sessionList.add(session);

                } catch (ParseException e) {
                    System.err.println("Error parsing date for session. Skipping this row.");
                }
            }
        }
        return sessionList;
    }

    // Load sessions with minimal details (only Session ID and Session Name)
    public static List<Session> loadMinimalSessions() throws IOException {
        List<Session> sessionList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length < 2) {
                    System.err.println("Skipping invalid session row: " + line);
                    continue;
                }

                String sessionID = row[0].trim();
                String sessionName = row[1].trim();

                // Minimal session object
                Session session = new Session(sessionID, sessionName);
                sessionList.add(session);
            }
        }
        return sessionList;
    }

    // Get session by ID from the session map
    public static Session loadSessionByID(String sessionID) {
        Session session = sessionMap.get(sessionID);
        if (session == null) {
            System.err.println("Session with ID " + sessionID + " not found.");
        }
        return session;
    }

    // Save all sessions back to the CSV file
    public static void saveAllSessions(List<Session> sessions) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SESSION_FILE))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            for (Session session : sessions) {
                bw.write(String.join(",",
                        session.getSessionID(),
                        session.getSessionName(),
                        session.getSpeaker().getName(),
                        dateFormat.format(session.getDate()),
                        session.getTime(),
                        session.getRoom()));
                bw.newLine();
            }
        }
    }

    // Add a new session to the CSV file and session map
    public static void addSession(Session session) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SESSION_FILE, true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            bw.write(String.join(",",
                    session.getSessionID(),
                    session.getSessionName(),
                    session.getSpeaker().getName(),
                    dateFormat.format(session.getDate()),
                    session.getTime(),
                    session.getRoom()));
            bw.newLine();
        }
        sessionMap.put(session.getSessionID(), session);
    }
}

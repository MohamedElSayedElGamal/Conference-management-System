import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionDatabase {

    private static final String SESSION_FILE = "sessions.csv";

    public static List<Session> loadSessions() throws IOException {
        List<Session> sessionList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String sessionID = row[0];
                String sessionName = row[1];
                String speakerName = row[2];
                Date date = null;
                try
                {
                    date = dateFormat.parse(row[3]);
                }
                catch (ParseException pe)
                {
                    System.err.println("Error parsing date for session: " + sessionID + ". Skipping this session.");
                    continue;
                }
                String time = row[4];
                String room = row[5];
                Speaker speaker = new Speaker(speakerName, "Bio"); // Placeholder bio
                Session session = new Session(sessionID, sessionName, speaker, date, time, room);
                sessionList.add(session);
            }
        }
        return sessionList;
    }

    public static void saveSessions(List<Session> sessions) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SESSION_FILE))) {
            for (Session session : sessions) {
                bw.write(session.getSessionID() + "," + session.getSessionName() + "," + session.getSpeaker().getName()
                        + "," + new SimpleDateFormat("MM/dd/yyyy").format(session.getDate()) + "," + session.getTime()
                        + "," + session.getRoom());
                bw.newLine();
            }
        }
    }

    public static void addSession(Session session) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SESSION_FILE, true))) {
            bw.write(session.getSessionID() + "," + session.getSessionName() + "," + session.getSpeaker().getName()
                    + "," + new SimpleDateFormat("MM/dd/yyyy").format(session.getDate()) + "," + session.getTime()
                    + "," + session.getRoom());
            bw.newLine();
        }
    }
}

import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConferenceDatabase {

    private static final String CONFERENCE_FILE = "conference.csv";

    // Method to load conferences from the CSV file
    public static List<Conference> loadConferences() throws IOException {
        List<Conference> conferenceList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CONFERENCE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String name = row[0];
                Date startDate = new SimpleDateFormat("MM/dd/yyyy").parse(row[1]);

                Date endDate = null;
                try {
                    endDate = new SimpleDateFormat("MM/dd/yyyy").parse(row[2]);
                } catch (ParseException pe) {
                    pe.printStackTrace();  // Handle the exception
                }

                if (endDate != null) {
                    // Only add the conference if endDate is valid
                    Conference conference = new Conference(name, startDate, endDate);
                    conferenceList.add(conference);
                } else {
                    System.out.println("Invalid end date for conference: " + name);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();  // Handle IO and Parse exceptions
        }
        return conferenceList;
    }

    // Method to save conferences to the CSV file
    public static void saveConferences(List<Conference> conferenceList) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFERENCE_FILE))) {
            for (Conference conference : conferenceList) {
                bw.write(conference.getName() + ","
                        + new SimpleDateFormat("MM/dd/yyyy").format(conference.getStartDate()) 
                        + ","
                        + new SimpleDateFormat("MM/dd/yyyy").format(conference.getEndDate()));
                bw.newLine();
            }
        }
    }

    // Method to add a new conference to the CSV file
    public static void addConference(Conference conference) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFERENCE_FILE, true))) {
            bw.write(conference.getName() + ","
                    + new SimpleDateFormat("MM/dd/yyyy").format(conference.getStartDate())
                    + ","
                    + new SimpleDateFormat("MM/dd/yyyy").format(conference.getEndDate()));
            bw.newLine();
        }
    }
}

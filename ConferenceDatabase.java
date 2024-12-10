import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConferenceDatabase {

    private static final String CONFERENCE_FILE = "C:\\New folder\\Java_project\\conference.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    // Load all conferences from the CSV file
    public static List<Conference> loadConferences() throws IOException {
        List<Conference> conferenceList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CONFERENCE_FILE))) {
            String line;
            br.readLine(); // Skip header if CSV has one
            while ((line = br.readLine()) != null) {
                try {
                    String[] row = line.split(",");
                    if (row.length < 3) {
                        System.err.println("Skipping invalid line: " + line + " | Not enough columns");
                        continue; // Skip invalid row
                    }

                    String name = row[0].trim();
                    Date startDate = DATE_FORMAT.parse(row[1].trim());
                    Date endDate = DATE_FORMAT.parse(row[2].trim());

                    conferenceList.add(new Conference(name, startDate, endDate));
                } catch (ParseException e) {
                    System.err.println("Skipping invalid date format: " + line + " | Error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid line: " + line + " | Error: " + e.getMessage());
                }
            }
        }
        return conferenceList;
    }

    // Save all conferences to the CSV file, overwriting the existing file
    public static void saveConferences(List<Conference> conferenceList) throws IOException {
        if (conferenceList == null || conferenceList.isEmpty()) {
            System.out.println("No conferences to save.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFERENCE_FILE))) {
            // Write the header
            bw.write("Conference Name,Start Date,End Date");
            bw.newLine();

            // Write each conference's data
            for (Conference conference : conferenceList) {
                bw.write(formatConference(conference));
                bw.newLine();
            }
        }
    }

    // Add a new conference to the CSV file by appending it
    public static void addConference(Conference conference) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFERENCE_FILE, true))) {
            bw.write(formatConference(conference));
            bw.newLine();
        }
    }

    // Helper method to format the conference data into CSV string
    private static String formatConference(Conference conference) {
        return conference.getConferanceName() + ","
                + DATE_FORMAT.format(conference.getStartDate()) + ","
                + DATE_FORMAT.format(conference.getEndDate());
    }

    // Load management IDs from the conference database (the first column)
    public static List<String> loadManagementIDs() throws IOException {
        List<String> managementIDs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CONFERENCE_FILE))) {
            String line;
            br.readLine(); // Skip header if present
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length > 0) managementIDs.add(row[0].trim()); // Assuming management ID is the first column
            }
        }
        return managementIDs;
    }
}

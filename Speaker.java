import java.util.ArrayList;
import java.util.List;

public class Speaker {
    private String name;
    private String bio;
    private List<Session> sessions;

    // Constructor
    public Speaker(String name, String bio) {
        this.name = name;
        this.bio = bio;
        this.sessions = new ArrayList<>();
    }

    // Method to get speaker bio
    public String getBio() {
        return bio;
    }

    // Method to get associated sessions
    public List<Session> getAssociatedSessions() {
        return sessions;
    }

    // Getters
    public String getName() {
        return name;
    }
}

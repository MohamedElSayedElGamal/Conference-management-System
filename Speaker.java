import java.util.*;

public class Speaker {
    private String name;
    private String bio;
    private List<Session> sessions;

    public Speaker(String name, String bio) {
        this.name = name;
        this.bio = bio;
        this.sessions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public List<Session> getAssociatedSessions() {
        return sessions;
    }
}

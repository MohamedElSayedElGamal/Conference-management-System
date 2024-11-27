public class Feedback {
    private String attendeeID; // Linked to attendee
    private String comment;
    private int rating;

    public Feedback(String attendeeID, String comment, int rating) {
        this.attendeeID = attendeeID;
        this.comment = comment;
        this.rating = rating;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Comment: " + comment + ", Rating: " + rating;
    }
}

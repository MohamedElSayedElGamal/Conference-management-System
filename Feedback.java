public class Feedback {
    private String attendeeID;
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

    public void setAttendeeID(String attendeeID) {
        this.attendeeID = attendeeID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

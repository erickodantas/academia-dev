package academia_dev;

public class SupportTicket {
    private User author;
    private String title;
    private String message;

    public SupportTicket(User author, String title, String message) {
        this.author = author;
        this.title = title;
        this.message = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package pjatk.edu.pl.data.exception;

public class MediaNotFoundException extends RuntimeException {
    public MediaNotFoundException(Integer mediaId) {
        super("Media with ID: " + mediaId + " not found in database.");
    }

    public MediaNotFoundException(String message) {
        super(message);
    }

    public MediaNotFoundException() {
        super("Media not found in database.");
    }
}

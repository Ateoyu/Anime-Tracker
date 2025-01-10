package pjatk.edu.pl.data.exception;

public class MediaClientException extends RuntimeException {
    public MediaClientException(String message) {
        super("External API Error: " + message);
    }
}

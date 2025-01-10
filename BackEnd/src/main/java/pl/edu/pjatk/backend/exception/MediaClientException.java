package pl.edu.pjatk.backend.exception;

public class MediaClientException extends RuntimeException {
    public MediaClientException(String message) {
        super("External API Error: " + message);
    }
}

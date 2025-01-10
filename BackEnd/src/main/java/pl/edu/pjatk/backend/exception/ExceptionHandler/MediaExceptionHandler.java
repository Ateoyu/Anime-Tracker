package pl.edu.pjatk.backend.exception.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.pjatk.backend.exception.MediaAlreadyExistsException;
import pl.edu.pjatk.backend.exception.MediaClientException;
import pl.edu.pjatk.backend.exception.MediaNotFoundException;
import pl.edu.pjatk.backend.exception.MediaObjectIncomplete;

@ControllerAdvice
public class MediaExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {MediaAlreadyExistsException.class})
    public ResponseEntity<Object> handleAlreadyExists(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MediaNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MediaObjectIncomplete.class})
    public ResponseEntity<Object> handleIncomplete(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MediaClientException.class})
    public ResponseEntity<Object> handleClientError(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

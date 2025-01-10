package pl.edu.pjatk.backend.exception.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.pjatk.backend.exception.GenreAlreadyExistsException;

@ControllerAdvice
public class GenreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {GenreAlreadyExistsException.class})
    public ResponseEntity<Object> genreAlreadyExists(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}

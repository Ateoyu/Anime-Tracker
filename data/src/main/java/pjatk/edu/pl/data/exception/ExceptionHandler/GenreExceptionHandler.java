package pjatk.edu.pl.data.exception.ExceptionHandler;

import pjatk.edu.pl.data.exception.GenreAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GenreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {GenreAlreadyExistsException.class})
    public ResponseEntity<Object> genreAlreadyExists(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}

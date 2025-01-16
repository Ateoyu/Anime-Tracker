package pjatk.edu.pl.data.exception.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pjatk.edu.pl.data.exception.CustomMediaListIncompleteException;

@Slf4j
@ControllerAdvice
public class CustomMediaListExceptionHandler {

    @ExceptionHandler(value = {CustomMediaListIncompleteException.class})
    public ResponseEntity<Object> handleIncomplete(CustomMediaListIncompleteException ex) {
        log.error("CustomMediaList validation failed: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
} 
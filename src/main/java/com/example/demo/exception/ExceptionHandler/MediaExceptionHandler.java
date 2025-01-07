package com.example.demo.exception.ExceptionHandler;

import com.example.demo.exception.MediaAlreadyExistsException;
import com.example.demo.exception.MediaClientException;
import com.example.demo.exception.MediaNotFoundException;
import com.example.demo.exception.MediaObjectIncomplete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

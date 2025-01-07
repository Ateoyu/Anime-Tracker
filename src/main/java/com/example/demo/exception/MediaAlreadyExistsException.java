package com.example.demo.exception;

public class MediaAlreadyExistsException extends RuntimeException {
    public MediaAlreadyExistsException(Integer mediaId) {
        super("Media with ID: " + mediaId + " already exists in the database.");
    }
}

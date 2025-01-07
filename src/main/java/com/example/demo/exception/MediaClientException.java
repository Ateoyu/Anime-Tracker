package com.example.demo.exception;

public class MediaClientException extends RuntimeException {
    public MediaClientException(String message) {
        super("External API Error: " + message);
    }
}

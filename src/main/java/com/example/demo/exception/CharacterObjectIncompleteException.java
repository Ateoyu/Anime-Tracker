package com.example.demo.exception;

public class CharacterObjectIncompleteException extends RuntimeException {
    public CharacterObjectIncompleteException(String message) {
        super("Character object is incomplete.");
    }
}

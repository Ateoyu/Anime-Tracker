package com.example.demo.exception;

public class CharacterAlreadyExistsException extends RuntimeException {
  public CharacterAlreadyExistsException(Integer characterID) {
    super("Character with ID: " + characterID + " already exists in the database.");
  }
}

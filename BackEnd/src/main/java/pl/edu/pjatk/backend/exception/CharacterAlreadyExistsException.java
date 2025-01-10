package pl.edu.pjatk.backend.exception;

public class CharacterAlreadyExistsException extends RuntimeException {
  public CharacterAlreadyExistsException(Integer characterID) {
    super("Character with ID: " + characterID + " already exists in the database.");
  }
}

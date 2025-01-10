package pl.edu.pjatk.backend.exception;

public class CharacterNotFoundException extends RuntimeException {
  public CharacterNotFoundException(Integer characterID) {
    super("Character with ID: " + characterID + " not found in database.");
  }
}

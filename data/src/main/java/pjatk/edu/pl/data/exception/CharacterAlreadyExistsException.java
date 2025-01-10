package pjatk.edu.pl.data.exception;

public class CharacterAlreadyExistsException extends RuntimeException {
  public CharacterAlreadyExistsException(Integer characterID) {
    super("Character with ID: " + characterID + " already exists in the database.");
  }
}

package pl.edu.pjatk.backend.exception;

public class CharacterObjectIncompleteException extends RuntimeException {
    public CharacterObjectIncompleteException(String message) {
        super("Character object is incomplete.");
    }
}

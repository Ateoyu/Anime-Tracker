package pjatk.edu.pl.data.exception;

public class CharacterObjectIncompleteException extends RuntimeException {
    public CharacterObjectIncompleteException(String message) {
        super("Character object is incomplete.");
    }
}

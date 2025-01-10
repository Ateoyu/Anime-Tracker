package pjatk.edu.pl.data.exception;

public class GenreAlreadyExistsException extends RuntimeException {
    public GenreAlreadyExistsException(String genreName) {
        super("Genre with name: " + genreName + " already exists in the database.");
    }
}

import java.sql.SQLException;

public class LibraryException extends Exception {
    public LibraryException(String message, SQLException cause) {
        super(message, cause);
    }
}

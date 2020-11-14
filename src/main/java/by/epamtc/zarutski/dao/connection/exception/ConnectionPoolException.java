package by.epamtc.zarutski.dao.connection.exception;

/**
 * The class {@code ConnectionPoolException} defines an exception a connection pool can throw
 * when it encounters difficulty
 *
 * @author Maksim Zarutski
 */
public class ConnectionPoolException extends Exception {

    private static final long serialVersionUID = 8750536988658798577L;

    /**
     * Constructs new {@code ConnectionPoolException} object
     * with {@code null} as its error detail message
     */
    public ConnectionPoolException() {
        super();
    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(Exception e) {
        super(e);
    }

    public ConnectionPoolException(String message, Exception e) {
        super(message, e);
    }

}
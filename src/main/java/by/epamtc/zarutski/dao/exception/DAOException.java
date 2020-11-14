package by.epamtc.zarutski.dao.exception;

/**
 * The class {@code DAOException} defines an exception a DAO layer can throw
 * when it encounters difficulty
 * <p>
 * This class is the general class of exceptions produced
 * by failure or interruption of the DAO operations
 *
 * @author Maksim Zarutski
 */
public class DAOException extends Exception {

    private static final long serialVersionUID = -988771128410288201L;

    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Exception e) {
        super(e);
    }

    public DAOException(String message, Exception e) {
        super(message, e);
    }

}
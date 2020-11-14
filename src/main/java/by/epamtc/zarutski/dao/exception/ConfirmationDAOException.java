package by.epamtc.zarutski.dao.exception;

/**
 * The class {@code ConfirmationDAOException} defines an exception a method can throw
 * when further execution of the method is impossible due to mismatch of codes
 *
 * @author Maksim Zarutski
 */
public class ConfirmationDAOException extends DAOException {

    private static final long serialVersionUID = -4864894440105595609L;

    public ConfirmationDAOException() {
    }

    public ConfirmationDAOException(String message) {
        super(message);
    }

    public ConfirmationDAOException(Exception e) {
        super(e);
    }

    public ConfirmationDAOException(String message, Exception e) {
        super(message, e);
    }

}
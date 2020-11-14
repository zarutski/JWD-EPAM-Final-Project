package by.epamtc.zarutski.dao.exception;

/**
 * The {@code InsufficientFundsDaoException} class defines an exception that a method can throw
 * when there is insufficient funds to complete a transaction
 *
 * @author Maksim Zarutski
 */
public class InsufficientFundsDaoException extends DAOException {

    private static final long serialVersionUID = -8630957103955313080L;

    public InsufficientFundsDaoException() {
    }

    public InsufficientFundsDaoException(String message) {
        super(message);
    }

    public InsufficientFundsDaoException(Exception e) {
        super(e);
    }

    public InsufficientFundsDaoException(String message, Exception e) {
        super(message, e);
    }

}
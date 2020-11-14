package by.epamtc.zarutski.service.exception;

/**
 * The {@code InsufficientFundsServiceException} class defines an exception that a method will throw
 * if an {@code InsufficientFundsDaoException} object was thrown during dao operation
 *
 * @author Maksim Zarutski
 */
public class InsufficientFundsServiceException extends ServiceException {

    private static final long serialVersionUID = 1965418622931929995L;

    public InsufficientFundsServiceException() {
    }

    public InsufficientFundsServiceException(String message) {
        super(message);
    }

    public InsufficientFundsServiceException(Exception e) {
        super(e);
    }

    public InsufficientFundsServiceException(String message, Exception e) {
        super(message, e);
    }
}
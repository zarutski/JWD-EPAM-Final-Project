package by.epamtc.zarutski.service.exception;

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
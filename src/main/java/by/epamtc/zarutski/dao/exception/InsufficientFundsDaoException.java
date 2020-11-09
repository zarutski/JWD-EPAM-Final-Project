package by.epamtc.zarutski.dao.exception;

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
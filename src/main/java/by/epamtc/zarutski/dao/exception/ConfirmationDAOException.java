package by.epamtc.zarutski.dao.exception;

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
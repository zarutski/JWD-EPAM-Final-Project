package by.epamtc.zarutski.dao.exception;

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
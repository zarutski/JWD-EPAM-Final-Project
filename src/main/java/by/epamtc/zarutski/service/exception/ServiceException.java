package by.epamtc.zarutski.service.exception;

public class ServiceException extends Exception {

    private static final long serialVersionUID = -408164749836704657L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
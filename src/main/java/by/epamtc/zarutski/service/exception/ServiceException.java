package by.epamtc.zarutski.service.exception;

/**
 * The class {@code ServiceException} defines an exception a service layer can throw
 * when it encounters difficulty
 * <p>
 * This class is the general class of exceptions arising during service operations
 *
 * @author Maksim Zarutski
 */
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
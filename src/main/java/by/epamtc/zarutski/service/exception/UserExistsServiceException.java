package by.epamtc.zarutski.service.exception;

/**
 * The {@code UserExistsServiceException} class defines an exception that a method will throw
 * if an {@code UserExistsDAOException} object was thrown during dao operation
 *
 * @author Maksim Zarutski
 */
public class UserExistsServiceException extends ServiceException {

    private static final long serialVersionUID = 8568186940728078297L;

    public UserExistsServiceException() {
    }

    public UserExistsServiceException(String message) {
        super(message);
    }

    public UserExistsServiceException(Exception e) {
        super(e);
    }

    public UserExistsServiceException(String message, Exception e) {
        super(message, e);
    }
}
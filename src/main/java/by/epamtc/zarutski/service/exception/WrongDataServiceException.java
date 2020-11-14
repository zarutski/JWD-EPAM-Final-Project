package by.epamtc.zarutski.service.exception;

/**
 * The {@code WrongDataServiceException} class defines an exception that a method will throw
 * if an {@code WrongDataDAOException} object was thrown during dao operation
 *
 * @author Maksim Zarutski
 */
public class WrongDataServiceException extends ServiceException {

    private static final long serialVersionUID = 6908480306900254215L;

    public WrongDataServiceException() {
    }

    public WrongDataServiceException(String message) {
        super(message);
    }

    public WrongDataServiceException(Exception e) {
        super(e);
    }

    public WrongDataServiceException(String message, Exception e) {
        super(message, e);
    }
}
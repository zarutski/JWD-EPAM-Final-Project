package by.epamtc.zarutski.service.exception;

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
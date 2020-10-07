package by.epamtc.zarutski.service.exception;

public class WrongDataServiceException extends ServiceException {

	private static final long serialVersionUID = 6908480306900254215L;

	public WrongDataServiceException() {
    }

    public WrongDataServiceException(String message) {
        super(message);
    }

    public WrongDataServiceException( Exception e) {
        super(e);
    }

    public WrongDataServiceException(String message, Exception e) {
        super(message, e);
    }
}

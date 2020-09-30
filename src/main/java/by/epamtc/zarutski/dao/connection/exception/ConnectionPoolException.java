package by.epamtc.zarutski.dao.connection.exception;

public class ConnectionPoolException extends Exception {


	private static final long serialVersionUID = 8750536988658798577L;

	public ConnectionPoolException() {
		super();
	}

	public ConnectionPoolException(String message) {
		super(message);
	}

	public ConnectionPoolException(Exception e) {
		super(e);
	}
	
	public ConnectionPoolException(String message, Exception e) {
		super(message, e);
	}

}

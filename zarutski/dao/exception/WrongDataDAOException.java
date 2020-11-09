package by.epamtc.zarutski.dao.exception;

public class WrongDataDAOException extends DAOException {

	private static final long serialVersionUID = -1159745342170461328L;

	public WrongDataDAOException() {
    }

    public WrongDataDAOException(String message) {
        super(message);
    }

    public WrongDataDAOException(Exception e) {
        super(e);
    }

    public WrongDataDAOException(String message, Exception e) {
        super(message, e);
    }

}
package by.epamtc.zarutski.dao.exception;

/**
 * The {@code WrongDataDAOException} class defines an exception
 * that can be throw when using wrong data for DAO layer's methods
 *
 * @author Maksim Zarutski
 */
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
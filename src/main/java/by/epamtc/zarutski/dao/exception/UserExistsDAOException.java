package by.epamtc.zarutski.dao.exception;

/**
 * The {@code UserExistsDAOException} class defines an exception that can be throw
 * by the DAO layer, when the user who is trying to register already exists
 *
 * @author Maksim Zarutski
 */
public class UserExistsDAOException extends DAOException {

    private static final long serialVersionUID = 4432366633197895179L;

    public UserExistsDAOException() {
    }

    public UserExistsDAOException(String message) {
        super(message);
    }

    public UserExistsDAOException(Exception e) {
        super(e);
    }

    public UserExistsDAOException(String message, Exception e) {
        super(message, e);
    }

}
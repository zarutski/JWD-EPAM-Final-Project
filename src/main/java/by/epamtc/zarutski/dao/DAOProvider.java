package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.dao.impl.SQLUserDAOImpl;

public class DAOProvider {

    private static final DAOProvider instance = new DAOProvider();

    private final UserDAO userDAO = new SQLUserDAOImpl();

    private DAOProvider() {
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public static DAOProvider getInstance() {
        return instance;
    }
}

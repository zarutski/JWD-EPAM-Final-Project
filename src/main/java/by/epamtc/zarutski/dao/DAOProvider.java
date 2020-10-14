package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.dao.impl.SQLAccountDAOImpl;
import by.epamtc.zarutski.dao.impl.SQLCardDAOImpl;
import by.epamtc.zarutski.dao.impl.SQLUserDAOImpl;

public class DAOProvider {

    private static final DAOProvider instance = new DAOProvider();

    private final UserDAO userDAO = new SQLUserDAOImpl();
    private final CardDAO cardDAO = new SQLCardDAOImpl();
    private final AccountDAO accountDAO = new SQLAccountDAOImpl();

    private DAOProvider() {
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public CardDAO getCardDAO() {
        return cardDAO;
    }

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public static DAOProvider getInstance() {
        return instance;
    }
}

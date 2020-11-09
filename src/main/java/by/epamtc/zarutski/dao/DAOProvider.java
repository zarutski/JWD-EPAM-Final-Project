package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.dao.impl.UserDAOImpl;
import by.epamtc.zarutski.dao.impl.FacilityDAOImpl;
import by.epamtc.zarutski.dao.impl.FacilityActionDAOImpl;

public class DAOProvider {

    private static final DAOProvider instance = new DAOProvider();

    private final UserDAO userDAO = new UserDAOImpl();
    private final FacilityDAO facilityDAO = new FacilityDAOImpl();
    private final FacilityActionDAO facilityActionDAO = new FacilityActionDAOImpl();

    private DAOProvider() {
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public FacilityDAO getFacilityDAO() {
        return facilityDAO;
    }

    public FacilityActionDAO getFacilityActionDAO() {
        return facilityActionDAO;
    }

    public static DAOProvider getInstance() {
        return instance;
    }
}
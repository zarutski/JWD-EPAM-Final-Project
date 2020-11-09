package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.dao.impl.SQLFacilitiesDAOImpl;
import by.epamtc.zarutski.dao.impl.SQLFacilityActionDAOImpl;
import by.epamtc.zarutski.dao.impl.SQLUserDAOImpl;

public class DAOProvider {

    private static final DAOProvider instance = new DAOProvider();

    private final UserDAO userDAO = new SQLUserDAOImpl();
    private final FacilitiesDAO facilitiesDAO = new SQLFacilitiesDAOImpl();
    private final FacilityActionDAO facilityActionDAO = new SQLFacilityActionDAOImpl();

    private DAOProvider() {
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public FacilitiesDAO getFacilitiesDAO() {
        return facilitiesDAO;
    }

    public FacilityActionDAO getFacilityActionDAO() {
        return facilityActionDAO;
    }

    public static DAOProvider getInstance() {
        return instance;
    }
}

package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UserData;

public interface UserDAO {

	AuthenticationData authentication(String login, String password) throws DAOException;

    boolean registration(RegistrationData registrationData) throws DAOException;

	UserData getUserData(int userId, String roleName) throws DAOException;
}

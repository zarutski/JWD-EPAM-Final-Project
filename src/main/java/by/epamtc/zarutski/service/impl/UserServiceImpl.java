package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.UserDAO;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.UserExistsDAOException;
import by.epamtc.zarutski.bean.UserData;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.UserExistsServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import by.epamtc.zarutski.service.validation.CredentialValidator;
import by.epamtc.zarutski.service.validation.RegistrationParametersValidator;

public class UserServiceImpl implements UserService {

    private static final String WRONG_AUTHENTICATION_DATA_MESSAGE = "Incorrect authentication data input";
    private static final String WRONG_REGISTRATION_DATA_MESSAGE = "Incorrect registration data input";

    @Override
    public AuthenticationData authentication(String login, String password) throws ServiceException {

        if (!CredentialValidator.isCredentialCorrect(login, password)) {
            throw new WrongDataServiceException(WRONG_AUTHENTICATION_DATA_MESSAGE);
        }

        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

        AuthenticationData authenticationData = null;
        try {
            authenticationData = userDAO.authentication(login, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return authenticationData;
    }

    @Override
    public boolean registration(RegistrationData registrationData) throws ServiceException {

        if (!RegistrationParametersValidator.registrationDataValidation(registrationData)) {
            throw new WrongDataServiceException(WRONG_REGISTRATION_DATA_MESSAGE);
        }

        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

        try {
            return userDAO.registration(registrationData);
        } catch (UserExistsDAOException e) {
            throw new UserExistsServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserData getUserData(int userId, String roleName) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

        UserData userData = null;
        try {
            userData = userDAO.getUserData(userId, roleName);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userData;
    }
}
package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.bean.UpdateUserData;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.UserDAO;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.UserExistsDAOException;
import by.epamtc.zarutski.bean.UserData;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.UserExistsServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import by.epamtc.zarutski.service.validation.CredentialValidator;
import by.epamtc.zarutski.service.validation.ParametersValidator;
import org.apache.commons.fileupload.FileItem;

import java.util.List;


// TODO --- проверить логируются ли exception в коммандах (унифицировать)
public class UserServiceImpl implements UserService {

    private static final String WRONG_AUTHENTICATION_DATA_MESSAGE = "Incorrect authentication data input";
    private static final String WRONG_REGISTRATION_DATA_MESSAGE = "Incorrect registration data input";
    private static final String WRONG_DATA_MESSAGE = "Incorrect update data input";
    private static final String WRONG_EXTENSION_MESSAGE = "Wrong file extension";

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

        if (!ParametersValidator.registrationDataValidation(registrationData)) {
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
    public UserData getUserData(int userId) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

        UserData userData = null;
        try {
            userData = userDAO.getUserData(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userData;
    }

    @Override
    public List<UserData> findUsers(String searchRequest) throws ServiceException {

        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

        List<UserData> userData = null;
        try {
            userData = userDAO.findUsers(searchRequest);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userData;
    }

    @Override
    public void updateUser(UpdateUserData userData) throws ServiceException {
        if (!ParametersValidator.userDataValidation(userData)) {
            throw new WrongDataServiceException(WRONG_DATA_MESSAGE);
        }

        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

        try {
            userDAO.updateUser(userData);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void changeUserRole(int userId, int roleCode) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO dao = daoProvider.getUserDAO();

        try {
            dao.changeUserRole(userId, roleCode);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void uploadUserPhoto(FileItem item, int userId) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO dao = daoProvider.getUserDAO();

        if (!ParametersValidator.extensionValidation(item)) {
            throw new WrongDataServiceException(WRONG_EXTENSION_MESSAGE);
        }

        try {
            dao.uploadUserPhoto(item, userId);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
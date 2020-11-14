package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.bean.UpdateUserData;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.UserExistsServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import by.epamtc.zarutski.service.validation.CredentialValidator;
import by.epamtc.zarutski.service.validation.ParametersValidator;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.UserDAO;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.UserExistsDAOException;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;

import org.apache.commons.fileupload.FileItem;

import java.util.List;

/**
 * The class {@code UserServiceImpl} provides implementation of the {@code UserService} interface
 *
 * @author Maksim Zarutski
 */
public class UserServiceImpl implements UserService {

    private static final String WRONG_AUTHENTICATION_DATA_MESSAGE = "Incorrect authentication data input";
    private static final String WRONG_REGISTRATION_DATA_MESSAGE = "Incorrect registration data input";
    private static final String WRONG_DATA_MESSAGE = "Incorrect update data input";
    private static final String WRONG_EXTENSION_MESSAGE = "Wrong file extension";

    /**
     * Performs user's authentication
     * <p>
     * Operation can be performed only after successful data validation
     * <p>
     * Returns {@code AuthenticationData} object, that represents user's authentication data
     * Returns null value if user was not found
     *
     * @param login    client's login value
     * @param password client's password value
     * @return {@code AuthenticationData} object, that represents user's authentication data
     * @throws WrongDataServiceException if invalid authentication data was provided
     * @throws ServiceException          if an error occurs during authentication process
     */
    @Override
    public AuthenticationData authentication(String login, String password) throws ServiceException {
        if (!CredentialValidator.isCredentialCorrect(login, password)) {
            throw new WrongDataServiceException(WRONG_AUTHENTICATION_DATA_MESSAGE);
        }

        AuthenticationData authenticationData = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            UserDAO userDAO = daoProvider.getUserDAO();

            authenticationData = userDAO.authentication(login, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return authenticationData;
    }

    /**
     * Performs user's registration using data from {@code RegistrationData} object
     * <p>
     * Operation can be performed only after successful data validation
     *
     * @param registrationData {@code RegistrationData} object containing client's data for registration
     * @return boolean value that indicates if the registration was completed successfully
     * @throws WrongDataServiceException if invalid registration data was provided
     * @throws ServiceException          if an error occurs during registration process
     */
    @Override
    public boolean registration(RegistrationData registrationData) throws ServiceException {
        if (!ParametersValidator.registrationDataValidation(registrationData)) {
            throw new WrongDataServiceException(WRONG_REGISTRATION_DATA_MESSAGE);
        }

        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            UserDAO userDAO = daoProvider.getUserDAO();

            return userDAO.registration(registrationData);
        } catch (UserExistsDAOException e) {
            throw new UserExistsServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Requesting data of the user by user's id
     * <p>
     * Returns {@code UserData} object containing data about the requested user
     * Returns null value if user was not found
     *
     * @param userId value is the identifier of the user whose data is requested
     * @return {@code UserData} object containing data about the requested user
     * @throws ServiceException if an error occurs during requesting user's data
     */
    @Override
    public UserData getUserData(int userId) throws ServiceException {

        UserData userData = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            UserDAO userDAO = daoProvider.getUserDAO();

            userData = userDAO.getUserData(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userData;
    }

    /**
     * Performs search of the users by the part of the full name or by passport full number
     * <p>
     * Returns empty list if no users were find
     * Or returns null if search request doesn't exist
     *
     * @param searchRequest value contains search request for searching user
     * @return list of the {@code UserData} objects matching search query
     * @throws ServiceException if an error occurs during operation
     */
    @Override
    public List<UserData> findUsers(String searchRequest) throws ServiceException {
        if (searchRequest == null || searchRequest.isEmpty()) {
            return null;
        }

        List<UserData> userData = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            UserDAO userDAO = daoProvider.getUserDAO();

            userData = userDAO.findUsers(searchRequest);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userData;
    }

    /**
     * Updates user's data in DB with received data
     * <p>
     * Operation can be performed only after successful data validation
     *
     * @param userData {@code UserData} object contains data for updating user's data
     * @throws WrongDataServiceException if invalid user's data was provided
     * @throws ServiceException          if an error occurs during operation
     */
    @Override
    public void updateUser(UpdateUserData userData) throws ServiceException {
        if (!ParametersValidator.userDataValidation(userData)) {
            throw new WrongDataServiceException(WRONG_DATA_MESSAGE);
        }

        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            UserDAO userDAO = daoProvider.getUserDAO();

            userDAO.updateUser(userData);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Changes user's role by received role value
     *
     * @param userId   value is the identifier of the user whose role will be changed
     * @param roleCode value of the new user's role
     * @throws ServiceException if an error occurs during operation
     */
    @Override
    public void changeUserRole(int userId, int roleCode) throws ServiceException {
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            UserDAO dao = daoProvider.getUserDAO();

            dao.changeUserRole(userId, roleCode);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Uploads received photo item for the certain user
     * <p>
     * Operation can be performed only after successful extension validation
     *
     * @param item   is a {@code FileItem} contains user's photo to upload
     * @param userId value is the identifier of the user
     * @throws WrongDataServiceException if invalid data was provided
     * @throws ServiceException          if an error occurs during uploading photo
     */
    @Override
    public void uploadUserPhoto(FileItem item, int userId) throws ServiceException {
        if (!ParametersValidator.extensionValidation(item)) {
            throw new WrongDataServiceException(WRONG_EXTENSION_MESSAGE);
        }

        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            UserDAO dao = daoProvider.getUserDAO();

            dao.uploadUserPhoto(item, userId);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.UpdateUserData;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.dao.exception.DAOException;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

/**
 * The interface {@code UserDAO} of the DAO layer for user's operations
 *
 * @author Maksim Zarutski
 */
public interface UserDAO {

    /**
     * Performs user's authentication
     * <p>
     * Returns {@code AuthenticationData} object, that represents user's authentication data
     * Returns null value if user was not found
     *
     * @param login    client's login value
     * @param password client's password value
     * @return {@code AuthenticationData} object, that represents user's authentication data
     * @throws DAOException if an error occurs during authentication process
     */
    AuthenticationData authentication(String login, String password) throws DAOException;

    /**
     * Performs user's registration using data from {@code RegistrationData} object
     *
     * @param registrationData {@code RegistrationData} object containing client's data for registration
     * @return boolean value that indicates if the registration was completed successfully
     * @throws DAOException if an error occurs during registration process
     */
    boolean registration(RegistrationData registrationData) throws DAOException;

    /**
     * Requesting data of the user by user's id
     * <p>
     * Returns {@code UserData} object containing data about the requested user
     * Returns null value if user was not found
     *
     * @param userId value is the identifier of the user whose data is requested
     * @return {@code UserData} object containing data about the requested user
     * @throws DAOException if an error occurs during requesting user's data
     */
    UserData getUserData(int userId) throws DAOException;

    /**
     * Performs search of the users by the part of the full name or by passport full number
     * <p>
     * Returns empty list if no users were find
     *
     * @param searchRequest value contains search request for searching user
     * @return list of the {@code UserData} objects matching search query
     * @throws DAOException if an error occurs during operation
     */
    List<UserData> findUsers(String searchRequest) throws DAOException;

    /**
     * Updates user's data in DB with received data
     *
     * @param userData {@code UserData} object contains data for updating user's data
     * @throws DAOException if an error occurs during operation
     */
    void updateUser(UpdateUserData userData) throws DAOException;

    /**
     * Changes user's role by received role value
     *
     * @param userId   value is the identifier of the user whose role will be changed
     * @param roleCode value of the new user's role
     * @throws DAOException if an error occurs during operation
     */
    void changeUserRole(int userId, int roleCode) throws DAOException;


    /**
     * Uploads received photo item for the certain user
     *
     * @param item   is a {@code FileItem} contains user's photo to upload
     * @param userId value is the identifier of the user
     * @throws DAOException if an error occurs during uploading photo
     */
    void uploadUserPhoto(FileItem item, int userId) throws DAOException;

}
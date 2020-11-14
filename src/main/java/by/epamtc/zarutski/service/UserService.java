package by.epamtc.zarutski.service;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UpdateUserData;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.service.exception.ServiceException;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

/**
 * The interface {@code UserService} of the service layer for user's operations
 *
 * @author Maksim Zarutski
 */
public interface UserService {

    AuthenticationData authentication(String login, String password) throws ServiceException;

    boolean registration(RegistrationData registrationData) throws ServiceException;

    UserData getUserData(int userId) throws ServiceException;

    List<UserData> findUsers(String searchRequest) throws ServiceException;

    void updateUser(UpdateUserData userData) throws ServiceException;

    void changeUserRole(int userId, int roleCode) throws ServiceException;

    void uploadUserPhoto(FileItem item, int userId) throws ServiceException;
}
package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.UpdateUserData;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UserData;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

public interface UserDAO {

    AuthenticationData authentication(String login, String password) throws DAOException;

    boolean registration(RegistrationData registrationData) throws DAOException;

    UserData getUserData(int userId) throws DAOException;

    List<UserData> findUsers(String searchRequest) throws DAOException;

    void updateUser(UpdateUserData userData) throws DAOException;

    void changeUserRole(int userId, int roleCode) throws DAOException;

    void uploadUserPhoto(FileItem item, int userId) throws DAOException;
}
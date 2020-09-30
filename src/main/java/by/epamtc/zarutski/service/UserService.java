package by.epamtc.zarutski.service;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;

public interface UserService {

    AuthenticationData authentication(String login, String password) throws ServiceException, WrongDataServiceException;

    boolean registration(RegistrationData registrationData) throws ServiceException;

	UserData getUserData(int userId, String roleName) throws ServiceException;

}

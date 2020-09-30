package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.UserDAO;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import by.epamtc.zarutski.service.validation.CredentialValidator;

public class UserServiceImpl implements UserService {

    private static final String MESSAGE = "SOME";
    private static final String MESSAGE_OTHER = "OTHER";

    @Override
    public AuthenticationData authentication(String login, String password) throws ServiceException, WrongDataServiceException {

        // исключения бросаются если логин и пароль некорректны (содержит недопустимые символы, короткие) чтобы не нагружать систему
    	// продумать ещё раз
        if (!CredentialValidator.isCorrect(login, password)) {
             throw new WrongDataServiceException(MESSAGE);
        }

        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

        AuthenticationData authenticationData = null;
        try {
            authenticationData = userDAO.authentication(login,password);
        } catch (DAOException e) {
            throw new ServiceException(MESSAGE_OTHER);
        }

        
        return authenticationData;
    }

    @Override
    public boolean registration(RegistrationData registrationData) throws ServiceException {

        // TODO --- валидация входных данных
        // TODO --- вынести в поля класса
        DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();

		try {
			return userDAO.registration(registrationData);
		} catch (DAOException e) {
			throw new ServiceException(MESSAGE_OTHER);
		}
    }

	@Override
	public UserData getUserData(int userId, String roleName)  throws ServiceException{
		DAOProvider daoProvider = DAOProvider.getInstance();
        UserDAO userDAO = daoProvider.getUserDAO();
        
        UserData userData = null;
        try {
            userData = userDAO.getUserData(userId, roleName);
        } catch (DAOException e) {
            throw new ServiceException(MESSAGE_OTHER);
        }

        return userData;
	}
}

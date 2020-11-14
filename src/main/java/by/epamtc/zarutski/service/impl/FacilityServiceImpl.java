package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.dao.FacilityDAO;
import by.epamtc.zarutski.service.FacilityService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;

import java.util.List;

/**
 * The class {@code FacilityServiceImpl} provides
 * implementation of the {@code FacilityService} interface
 *
 * @author Maksim Zarutski
 */
public class FacilityServiceImpl implements FacilityService {

    /**
     * Requests list of the user's cards by user's id
     * <p>
     * Returns empty list if no user's cards were find
     *
     * @param userId value is the identifier of the user whose cards will be requested
     * @return list of the {@code Card} objects of the user
     * @throws ServiceException if an error occurs during requesting user's cards
     */
    @Override
    public List<Card> getUserCards(int userId) throws ServiceException {

        List<Card> userCards = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

            userCards = facilityDAO.getUserCards(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userCards;
    }

    /**
     * Requests list of the user's accounts by user's id or by card's id
     * <p>
     * Returns empty list if no user's accounts were find
     *
     * @param id          value is the identifier of the user or user's card
     * @param destination value determines with which identifier the request will be executed
     * @return list of the {@code Account} objects
     * @throws ServiceException if an error occurs during requesting accounts
     */
    @Override
    public List<Account> getAccounts(int id, String destination) throws ServiceException {

        List<Account> userAccounts = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

            userAccounts = facilityDAO.getAccounts(id, destination);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userAccounts;
    }

    /**
     * Requests user's account {@code Account} object by user's id
     *
     * @param id     value is the identifier of the requested account
     * @param userId value is the identifier of the user whose account will be requested
     * @return {@code Account} object containing user's account data
     * @throws WrongDataServiceException if invalid parameters for operation were provided
     * @throws ServiceException          if an error occurs during requesting account
     */
    @Override
    public Account getAccById(int id, int userId) throws ServiceException {

        Account account = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

            account = facilityDAO.getAccById(id, userId);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return account;
    }

    /**
     * Requests user's card {@code Card} object by user's id
     *
     * @param userId value is the identifier of the user whose cards will be requested
     * @return {@code Card} object containing user's card data
     * @throws WrongDataServiceException if invalid parameters for operation were provided
     * @throws ServiceException          if an error occurs during requesting user's cards
     */
    @Override
    public Card getCardById(int cardId, int userId) throws ServiceException {

        Card userCard = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

            userCard = facilityDAO.getCardById(cardId, userId);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userCard;
    }

    /**
     * Requests list of the operations for user's card or user's account
     * <p>
     * Returns empty list if no operations were find
     *
     * @param id          value is the identifier of the user's card or user's account
     * @param destination value determines with which identifier the request will be executed
     * @return list of the {@code Operation} objects
     * @throws ServiceException if an error occurs during requesting operations
     */
    @Override
    public List<Operation> getOperations(int id, String destination) throws ServiceException {

        List<Operation> operations = null;
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

            operations = facilityDAO.getOperations(id, destination);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return operations;
    }
}
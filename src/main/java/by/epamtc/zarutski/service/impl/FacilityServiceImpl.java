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

public class FacilityServiceImpl implements FacilityService {

    @Override
    public List<Card> getUserCards(int userId) throws ServiceException {

        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

        List<Card> userCards = null;
        try {
            userCards = facilityDAO.getUserCards(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userCards;
    }

    @Override
    public List<Account> getAccounts(int id, String destination) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

        List<Account> userAccounts = null;
        try {
            userAccounts = facilityDAO.getAccounts(id, destination);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userAccounts;
    }

    @Override
    public Account getAccById(int id, int userId) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

        Account account = null;
        try {
            account = facilityDAO.getAccById(id, userId);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return account;
    }

    @Override
    public Card getCardById(int cardId, int userId) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

        Card userCard = null;
        try {
            userCard = facilityDAO.getCardById(cardId, userId);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userCard;
    }

    @Override
    public List<Operation> getOperations(int id, String destination) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityDAO facilityDAO = daoProvider.getFacilityDAO();

        List<Operation> operations = null;
        try {
            operations = facilityDAO.getOperations(id, destination);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return operations;
    }
}
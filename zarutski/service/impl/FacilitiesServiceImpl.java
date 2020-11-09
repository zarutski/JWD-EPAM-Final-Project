package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.dao.FacilitiesDAO;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;
import by.epamtc.zarutski.service.FacilitiesService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;

import java.util.List;

public class FacilitiesServiceImpl implements FacilitiesService {

    @Override
    public List<Card> getUserCards(int userId) throws ServiceException {

        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilitiesDAO facilitiesDAO = daoProvider.getFacilitiesDAO();

        List<Card> userCards = null;
        try {
            userCards = facilitiesDAO.getUserCards(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userCards;
    }

    @Override
    public List<Account> getAccounts(int id, String destination) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilitiesDAO facilitiesDAO = daoProvider.getFacilitiesDAO();

        List<Account> userAccounts = null;
        try {
            userAccounts = facilitiesDAO.getAccounts(id, destination);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userAccounts;
    }

    @Override
    public Account getAccById(int id, int userId) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilitiesDAO facilitiesDAO = daoProvider.getFacilitiesDAO();

        Account account = null;
        try {
            account = facilitiesDAO.getAccById(id, userId);
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
        FacilitiesDAO facilitiesDAO = daoProvider.getFacilitiesDAO();

        Card userCard = null;
        try {
            userCard = facilitiesDAO.getCardById(cardId, userId);
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
        FacilitiesDAO facilitiesDAO = daoProvider.getFacilitiesDAO();

        List<Operation> operations = null;
        try {
            operations = facilitiesDAO.getOperations(id, destination);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return operations;
    }
}
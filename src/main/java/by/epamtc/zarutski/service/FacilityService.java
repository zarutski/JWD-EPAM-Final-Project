package by.epamtc.zarutski.service;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.service.exception.ServiceException;

import java.util.List;

/**
 * The interface {@code FacilityService} of the service layer providing access to payment facilities information
 *
 * @author Maksim Zarutski
 */
public interface FacilityService {

    List<Card> getUserCards(int userId) throws ServiceException;

    List<Account> getAccounts(int id, String destination) throws ServiceException;

    Account getAccById(int id, int userId) throws ServiceException;

    Card getCardById(int cardId, int userId) throws ServiceException;

    List<Operation> getOperations(int id, String destination) throws ServiceException;
}
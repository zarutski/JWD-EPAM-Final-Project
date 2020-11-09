package by.epamtc.zarutski.service;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.service.exception.ServiceException;

import java.util.List;

public interface FacilitiesService {

    List<Card> getUserCards(int userId) throws ServiceException;

    List<Account> getAccounts(int id, String destination) throws ServiceException;

    Account getAccById(int id, int userId) throws ServiceException;

    Card getCardById(int cardId, int userId) throws ServiceException;

    List<Operation> getOperations(int id, String destination) throws ServiceException;
}

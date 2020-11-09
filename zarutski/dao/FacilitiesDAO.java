package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.dao.exception.DAOException;

import java.util.List;

public interface FacilitiesDAO {

    List<Card> getUserCards(int userId) throws DAOException;

    Card getCardById(int cardId, int userId) throws DAOException;

    List<Account> getAccounts(int id, String destination) throws DAOException;

    Account getAccById(int id, int userId) throws DAOException;

    List<Operation> getOperations(int id, String destination) throws DAOException;

}
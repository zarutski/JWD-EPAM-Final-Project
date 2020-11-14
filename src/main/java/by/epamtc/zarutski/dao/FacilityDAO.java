package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.dao.exception.DAOException;

import java.util.List;

/**
 * The interface {@code FacilityDAO} of the DAO layer providing access to payment facilities information
 *
 * @author Maksim Zarutski
 */
public interface FacilityDAO {

    /**
     * Requests list of the user's cards by user's id
     * <p>
     * Returns empty list if no user's cards were find
     *
     * @param userId value is the identifier of the user whose cards will be requested
     * @return list of the {@code Card} objects of the user
     * @throws DAOException if an error occurs during requesting user's cards
     */
    List<Card> getUserCards(int userId) throws DAOException;


    /**
     * Requests user's card {@code Card} object by user's id
     *
     * @param userId value is the identifier of the user whose cards will be requested
     * @return {@code Card} object containing user's card data
     * @throws DAOException if an error occurs during requesting user's cards
     */
    Card getCardById(int cardId, int userId) throws DAOException;

    /**
     * Requests list of the user's accounts by user's id or by card's id
     * <p>
     * Returns empty list if no user's accounts were find
     *
     * @param id          value is the identifier of the user or user's card
     * @param destination value determines with which identifier the request will be executed
     * @return list of the {@code Account} objects
     * @throws DAOException if an error occurs during requesting accounts
     */
    List<Account> getAccounts(int id, String destination) throws DAOException;

    /**
     * Requests user's account {@code Account} object by user's id
     *
     * @param id     value is the identifier of the requested account
     * @param userId value is the identifier of the user whose account will be requested
     * @return {@code Account} object containing user's account data
     * @throws DAOException if an error occurs during requesting account
     */
    Account getAccById(int id, int userId) throws DAOException;

    /**
     * Requests list of the operations for user's card or user's account
     * <p>
     * Returns empty list if no operations were find
     *
     * @param id          value is the identifier of the user's card or user's account
     * @param destination value determines with which identifier the request will be executed
     * @return list of the {@code Operation} objects
     * @throws DAOException if an error occurs during requesting operations
     */
    List<Operation> getOperations(int id, String destination) throws DAOException;

}
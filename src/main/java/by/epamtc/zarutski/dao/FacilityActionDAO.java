package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.AccOrderData;
import by.epamtc.zarutski.bean.CardOrderData;
import by.epamtc.zarutski.bean.TransferData;
import by.epamtc.zarutski.dao.exception.DAOException;

/**
 * The interface {@code FacilityActionDAO} of the DAO layer providing actions with payment facilities
 *
 * @author Maksim Zarutski
 */
public interface FacilityActionDAO {

    /**
     * Performs transfer operation from user's card to destination card,
     * or from one account to another
     *
     * @param transferData {@code TransferData} object contains data to perform transfer
     * @return boolean value that indicates if the transfer was completed successfully
     * @throws DAOException if an error occurs during transfer operation
     */
    boolean transfer(TransferData transferData) throws DAOException;

    /**
     * Performs ordering a new card for user
     *
     * @param cardOrderData {@code CardOrderData} object contains data for ordering new card
     * @param accOrderData  {@code AccOrderData} object contains data for ordering new account
     * @return boolean value that indicates if the card order was completed successfully
     * @throws DAOException if an error occurs during card order
     */
    boolean orderNewCard(CardOrderData cardOrderData, AccOrderData accOrderData) throws DAOException;

    /**
     * Performs changing user's card state
     *
     * @param cardId    value is the identifier of the card
     * @param cardState value contains new card state
     * @throws DAOException if an error occurs during operation
     */
    void changeCardState(int cardId, int cardState) throws DAOException;

}
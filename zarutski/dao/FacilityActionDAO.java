package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.AccOrderData;
import by.epamtc.zarutski.bean.CardOrderData;
import by.epamtc.zarutski.bean.TransferData;
import by.epamtc.zarutski.dao.exception.DAOException;

public interface FacilityActionDAO {

    boolean transfer(TransferData transferData) throws DAOException;

    boolean orderNewCard(CardOrderData cardOrderData, AccOrderData accOrderData) throws DAOException;

    void changeCardState(int cardId, int cardState) throws DAOException;

}
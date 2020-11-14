package by.epamtc.zarutski.service;

import by.epamtc.zarutski.bean.AccOrderData;
import by.epamtc.zarutski.bean.CardOrderData;
import by.epamtc.zarutski.bean.TransferData;
import by.epamtc.zarutski.service.exception.ServiceException;

/**
 * The interface {@code FacilityActionService} of the service layer providing actions with payment facilities
 *
 * @author Maksim Zarutski
 */
public interface FacilityActionService {

    boolean transfer(TransferData transferData) throws ServiceException;

    boolean orderNewCard(CardOrderData cardOrderData, AccOrderData accOrderData) throws ServiceException;

    void changeCardState(int cardId, int cardState) throws ServiceException;

}
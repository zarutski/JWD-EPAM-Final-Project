package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.dao.CardDAO;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.service.CardService;
import by.epamtc.zarutski.service.exception.ServiceException;

import java.util.List;

public class CardServiceImpl implements CardService {

    @Override
    public List<Card> getUserCards(int userId) throws ServiceException {

        DAOProvider daoProvider = DAOProvider.getInstance();
        CardDAO cardDAO = daoProvider.getCardDAO();

        List<Card> userCards = null;
        try {
            userCards = cardDAO.getUserCards(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userCards;
    }
}
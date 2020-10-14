package by.epamtc.zarutski.service;

import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.service.exception.ServiceException;

import java.util.List;

public interface CardService {

    List<Card> getUserCards(int userId) throws ServiceException;
}

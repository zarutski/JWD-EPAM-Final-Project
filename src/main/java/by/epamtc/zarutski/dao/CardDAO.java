package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.dao.exception.DAOException;

import java.util.List;

public interface CardDAO {

    List<Card> getUserCards(int userId) throws DAOException;
}

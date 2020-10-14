package by.epamtc.zarutski.dao;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.dao.exception.DAOException;

import java.util.List;

public interface AccountDAO {

    List<Account> getUserAccounts(int userId) throws DAOException;

}
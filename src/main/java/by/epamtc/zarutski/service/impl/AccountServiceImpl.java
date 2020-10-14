package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.dao.AccountDAO;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.service.AccountService;
import by.epamtc.zarutski.service.exception.ServiceException;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    @Override
    public List<Account> getUserAccounts(int userId) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        AccountDAO cardDAO = daoProvider.getAccountDAO();

        List<Account> userAccounts = null;
        try {
            userAccounts = cardDAO.getUserAccounts(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return userAccounts;
    }
}
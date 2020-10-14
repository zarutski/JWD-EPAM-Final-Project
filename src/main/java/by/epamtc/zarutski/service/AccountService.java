package by.epamtc.zarutski.service;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.service.exception.ServiceException;

import java.util.List;

public interface AccountService {

    List<Account> getUserAccounts(int userId) throws ServiceException;
}
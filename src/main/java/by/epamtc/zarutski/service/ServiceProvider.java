package by.epamtc.zarutski.service;

import by.epamtc.zarutski.service.impl.AccountServiceImpl;
import by.epamtc.zarutski.service.impl.CardServiceImpl;
import by.epamtc.zarutski.service.impl.UserServiceImpl;

public class ServiceProvider {

    private static final ServiceProvider instance = new ServiceProvider();

    private final UserService userService = new UserServiceImpl();
    private final CardService cardService = new CardServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();

    public static ServiceProvider getInstance() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public CardService getCardService() {
        return cardService;
    }

    public AccountService getAccountService() {
        return accountService;
    }
}
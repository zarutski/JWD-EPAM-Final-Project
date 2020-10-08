package by.epamtc.zarutski.service;

import by.epamtc.zarutski.service.impl.UserServiceImpl;

public class ServiceProvider {

    private static final ServiceProvider instance = new ServiceProvider();

    private final UserService impl = new UserServiceImpl();

    public static ServiceProvider getInstance() {
        return instance;
    }
    
    public UserService getUserService() {
        return impl;
    }
}
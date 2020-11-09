package by.epamtc.zarutski.service;

import by.epamtc.zarutski.service.impl.FacilitiesActionServiceImpl;
import by.epamtc.zarutski.service.impl.FacilitiesServiceImpl;
import by.epamtc.zarutski.service.impl.UserServiceImpl;

public class ServiceProvider {

    private static final ServiceProvider instance = new ServiceProvider();

    private final UserService userService = new UserServiceImpl();
    private final FacilitiesService facilitiesService = new FacilitiesServiceImpl();
    private final FacilitiesActionService facilitiesActionService = new FacilitiesActionServiceImpl();

    public static ServiceProvider getInstance() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public FacilitiesActionService getFacilitiesActionService() {
        return facilitiesActionService;
    }

    public FacilitiesService getFacilitiesService() {
        return facilitiesService;
    }

}
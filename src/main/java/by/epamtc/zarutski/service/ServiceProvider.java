package by.epamtc.zarutski.service;

import by.epamtc.zarutski.service.impl.FacilityActionServiceImpl;
import by.epamtc.zarutski.service.impl.FacilityServiceImpl;
import by.epamtc.zarutski.service.impl.UserServiceImpl;

/**
 * The class {@code ServiceProvider} provides an access to the service implementation objects
 *
 * @author Maksim Zarutski
 */
public class ServiceProvider {

    /**
     * Provider instance
     */
    private static final ServiceProvider instance = new ServiceProvider();

    private final UserService userService = new UserServiceImpl();
    private final FacilityService facilityService = new FacilityServiceImpl();
    private final FacilityActionService facilityActionService = new FacilityActionServiceImpl();

    public static ServiceProvider getInstance() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public FacilityActionService getFacilityActionService() {
        return facilityActionService;
    }

    public FacilityService getFacilityService() {
        return facilityService;
    }

}
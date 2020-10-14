package by.epamtc.zarutski.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;

public class GoToPersonalAreaCommand implements Command {

	private static final String PARAMETER_USER_DATA = "user_data";
    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    
    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ERROR_WRONG_AUTH_DATA = "login or password error";
    private static final String ERROR_SERVICE = "service error";

    private static final String PERSONAL_AREA_PAGE = "/WEB-INF/jsp/personalArea.jsp";
    private static final String AUTHENTICATION_PAGE = "/WEB-INF/jsp/authentication.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();

        // валидации
        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);
        String page;

        if (authenticationData != null) {
            int userId = authenticationData.getUserId();
            String userRoleName = authenticationData.getUserRole();

            UserData userData = null;
            try {
                userData = service.getUserData(userId, userRoleName);

                if (userData == null) {
                    request.setAttribute(ATTRIBUTE_ERROR, ERROR_WRONG_AUTH_DATA);
                    page = AUTHENTICATION_PAGE;
                } else {
                    request.setAttribute(PARAMETER_USER_DATA, userData);
                    page = PERSONAL_AREA_PAGE;
                }

            } catch (ServiceException e) {
                request.setAttribute(ATTRIBUTE_ERROR, ERROR_SERVICE);
                page = AUTHENTICATION_PAGE;
            }
        } else {
            page = AUTHENTICATION_PAGE;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}
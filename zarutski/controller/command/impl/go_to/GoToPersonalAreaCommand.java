package by.epamtc.zarutski.controller.command.impl.go_to;

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
    private static final String ERROR_SERVICE = "service error";

    private static final String PERSONAL_AREA_PAGE = "/WEB-INF/jsp/personalArea.jsp";
    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();

        // валидации
        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);
        String page = GO_TO_MAIN_PAGE;

        int userId = authenticationData.getUserId();
        UserData userData = null;

        try {
            userData = service.getUserData(userId);
            page = PERSONAL_AREA_PAGE;

            if (userData != null) {
                request.setAttribute(PARAMETER_USER_DATA, userData);
            }

        } catch (ServiceException e) {
            request.setAttribute(ATTRIBUTE_ERROR, ERROR_SERVICE);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}
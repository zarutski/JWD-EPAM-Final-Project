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

/**
 * The class {@code GoToPersonalAreaCommand} implements navigation to the user's personal area page.
 * <p>
 * Requests user's data by user id from request.
 *
 * @author Maksim Zarutski
 */
public class GoToPersonalAreaCommand implements Command {

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_USER_DATA = "user_data";

    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ERROR_SERVICE = "service error";

    private static final String PERSONAL_AREA_PAGE = "/WEB-INF/jsp/personalArea.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

        int userId = authenticationData.getUserId();
        UserData userData = null;

        try {
            ServiceProvider provider = ServiceProvider.getInstance();
            UserService service = provider.getUserService();

            userData = service.getUserData(userId);

            if (userData != null) {
                request.setAttribute(PARAMETER_USER_DATA, userData);
            }

        } catch (ServiceException e) {
            request.setAttribute(ATTRIBUTE_ERROR, ERROR_SERVICE);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(PERSONAL_AREA_PAGE);
        dispatcher.forward(request, response);
    }
}
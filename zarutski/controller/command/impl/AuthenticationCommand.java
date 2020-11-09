package by.epamtc.zarutski.controller.command.impl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;

public class AuthenticationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthenticationCommand.class);

    private static final String PARAMETER_LOGIN = "login";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";

    private static final String AMPERSAND = "&";
    private static final String PARAMETER_AUTHENTICATION_ERROR = "error=error_01";
    private static final String PARAMETER_SERVICE_ERROR = "error=error_02";

    private static final String GO_TO_PERSONAL_AREA = "controller?command=go_to_personal_area";
    private static final String GO_TO_AUTHENTICATION_PAGE = "controller?command=go_to_authentication_page";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();

        String login = request.getParameter(PARAMETER_LOGIN);
        String password = request.getParameter(PARAMETER_PASSWORD);

        AuthenticationData authenticationData = null;
        String page;

        try {
            authenticationData = service.authentication(login, password);

            if (authenticationData == null) {
                page = GO_TO_AUTHENTICATION_PAGE + AMPERSAND + PARAMETER_AUTHENTICATION_ERROR;
            } else {
                HttpSession session = request.getSession();
                session.setAttribute(PARAMETER_AUTHENTICATION_DATA, authenticationData);
                logger.info("User authenticated successfully");
                page = GO_TO_PERSONAL_AREA;
            }

        } catch (WrongDataServiceException e) {
            logger.info("Authentication data format isn't correct", e);
            page = GO_TO_AUTHENTICATION_PAGE + AMPERSAND + PARAMETER_AUTHENTICATION_ERROR;
        } catch (ServiceException e) {
            page = GO_TO_AUTHENTICATION_PAGE + AMPERSAND + PARAMETER_SERVICE_ERROR;
        }

        response.sendRedirect(page);
    }
}
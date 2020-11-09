package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class SearchUserCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SearchUserCommand.class);

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    public static final String PARAMETER_SEARCH_REQUEST = "search_request";
    public static final String PARAMETER_USERS = "users";

    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_SERVICE = "service_error";
    private static final String MESSAGE_NO_USERS = "no_users";

    public static final String LOG_SERVICE_ERROR = "service error";

    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";
    public static final String SEARCH_USER_PAGE = "/WEB-INF/jsp/searchUser.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);
        String page = GO_TO_MAIN_PAGE;


        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();
        page = SEARCH_USER_PAGE;

        String searchRequest = request.getParameter(PARAMETER_SEARCH_REQUEST);
        List<UserData> users = null;

        try {
            users = service.findUsers(searchRequest);

            if (users == null || users.isEmpty()) {
                request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_USERS);
            } else {
                request.setAttribute(PARAMETER_USERS, users);
            }

        } catch (ServiceException e) {
            logger.info(LOG_SERVICE_ERROR, e);
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_SERVICE);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}

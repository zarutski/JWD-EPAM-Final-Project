package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The class {@code SearchUserCommand} implements command for search of the user by received search query.
 * <p>
 * Search query may contain user name or surname part, or passport number.
 *
 * @author Maksim Zarutski
 */
public class SearchUserCommand implements Command {

    private static final String PARAMETER_SEARCH_REQUEST = "search_request";
    private static final String PARAMETER_USERS = "users";

    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_NO_USERS = "no_users";
    private static final String MESSAGE_SERVICE = "service_error";

    private static final String SEARCH_USER_PAGE = "/WEB-INF/jsp/searchUser.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String searchRequest = request.getParameter(PARAMETER_SEARCH_REQUEST);
        List<UserData> users = null;

        try {
            ServiceProvider provider = ServiceProvider.getInstance();
            UserService service = provider.getUserService();

            users = service.findUsers(searchRequest);

            if (users == null || users.isEmpty()) {
                request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_USERS);
            } else {
                request.setAttribute(PARAMETER_USERS, users);
            }

        } catch (ServiceException e) {
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_SERVICE);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(SEARCH_USER_PAGE);
        dispatcher.forward(request, response);
    }
}
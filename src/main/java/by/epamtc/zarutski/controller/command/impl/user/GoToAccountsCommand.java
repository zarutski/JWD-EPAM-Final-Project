package by.epamtc.zarutski.controller.command.impl.user;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.FacilityService;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GoToAccountsCommand implements Command {

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_USER_ACCOUNTS = "user_accounts";
    private static final String DESTINATION_USER = "user";

    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ERROR_SERVICE = "service_error";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_NO_ACCOUNTS = "no_accounts";

    private static final String ACCOUNTS_PAGE = "/WEB-INF/jsp/accounts.jsp";
    private static final String ACCOUNTS_LIST_PAGE = "/WEB-INF/jsp/accountsList.jsp";

    private static final String ACTION_PAYMENT = "payment";
    private static final String PARAMETER_ACTION = "action";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

        ServiceProvider provider = ServiceProvider.getInstance();
        FacilityService service = provider.getFacilityService();

        int userId = authenticationData.getUserId();
        List<Account> usersAccounts = null;

        try {
            usersAccounts = service.getAccounts(userId, DESTINATION_USER);

            if (usersAccounts == null || usersAccounts.isEmpty()) {
                request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_ACCOUNTS);
            } else {
                request.setAttribute(PARAMETER_USER_ACCOUNTS, usersAccounts);
            }

        } catch (ServiceException e) {
            request.setAttribute(ATTRIBUTE_ERROR, ERROR_SERVICE);
        }

        String action = request.getParameter(PARAMETER_ACTION);
        String page = getActionPage(action);

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    private String getActionPage(String action) {
        String page = null;
        if (ACTION_PAYMENT.equals(action)) {
            page = ACCOUNTS_LIST_PAGE;
        } else {
            page = ACCOUNTS_PAGE;
        }

        return page;
    }
}
package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.AccountService;
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
    
    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ERROR_SERVICE = "service error";
    private static final String ATTRIBUTE_MESSAGE = "message";  
    private static final String MESSAGE_NO_ACCOUNTS = "no accounts";

    private static final String GO_TO_AUTHENTICATION_PAGE = "controller?command=go_to_authentication_page";
    private static final String ACCOUNTS_PAGE = "/WEB-INF/jsp/accounts.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceProvider provider = ServiceProvider.getInstance();
        AccountService service = provider.getAccountService();

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

        if (authenticationData != null) {
            int userId = authenticationData.getUserId();

            List<Account> usersAccounts = null;
            try {
                usersAccounts = service.getUserAccounts(userId);

                if (usersAccounts == null) {
                    request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_ACCOUNTS);
                } else {
                    request.setAttribute(PARAMETER_USER_ACCOUNTS, usersAccounts);
                }

            } catch (ServiceException e) {
                request.setAttribute(ATTRIBUTE_ERROR, ERROR_SERVICE);
            }
        } else {
            response.sendRedirect(GO_TO_AUTHENTICATION_PAGE);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(ACCOUNTS_PAGE);
        dispatcher.forward(request, response);
    }
}
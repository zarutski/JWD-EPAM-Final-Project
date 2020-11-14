package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.FacilityService;
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
 * The class {@code GoToUserDetailsCommand} implements navigation to user's details page.
 * <p>
 * Requests data and forms a new request containing data about the user, as well as it's card and accounts
 *
 * @author Maksim Zarutski
 */
public class GoToUserDetailsCommand implements Command {

    private static final String GO_TO_SEARCH_USER = "controller?command=go_to_search_user";
    private static final String USER_DETAILS_PAGE = "/WEB-INF/jsp/userDetails.jsp";

    private static final String PARAMETER_USER_DATA = "user_data";
    private static final String PARAMETER_USER_CARDS = "user_cards";
    private static final String PARAMETER_USER_ACCOUNTS = "user_accounts";
    private static final String PARAMETER_USER_ID = "user_id";

    private static final String ATTRIBUTE_CARD_MESSAGE = "card_message";
    private static final String ATTRIBUTE_ACC_MESSAGE = "acc_message";
    private static final String MESSAGE_NO_CARDS = "no_cards";
    private static final String MESSAGE_NO_ACCOUNTS = "no_accounts";

    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_SERVICE = "service_error";
    private static final String MESSAGE_NO_USERS = "wrong_user";

    private static final String DESTINATION_USER = "user";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userDetailsId = Integer.parseInt(request.getParameter(PARAMETER_USER_ID));
        String page = GO_TO_SEARCH_USER;

        List<Account> usersAccounts = null;
        List<Card> usersCards = null;
        UserData userData = null;

        try {
            ServiceProvider provider = ServiceProvider.getInstance();
            FacilityService facilityService = provider.getFacilityService();
            UserService service = provider.getUserService();

            usersAccounts = facilityService.getAccounts(userDetailsId, DESTINATION_USER);
            setAccAttribute(request, usersAccounts);

            usersCards = facilityService.getUserCards(userDetailsId);
            setCardsAttribute(request, usersCards);

            userData = service.getUserData(userDetailsId);
            if (userData != null) {
                request.setAttribute(PARAMETER_USER_DATA, userData);
                page = USER_DETAILS_PAGE;
            } else {
                request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_USERS);
            }

        } catch (ServiceException e) {
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_SERVICE);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    private void setAccAttribute(HttpServletRequest request, List<Account> usersAccounts) {
        if (usersAccounts == null || usersAccounts.isEmpty()) {
            request.setAttribute(ATTRIBUTE_ACC_MESSAGE, MESSAGE_NO_ACCOUNTS);
        } else {
            request.setAttribute(PARAMETER_USER_ACCOUNTS, usersAccounts);
        }
    }

    private void setCardsAttribute(HttpServletRequest request, List<Card> usersCards) {
        if (usersCards == null || usersCards.isEmpty()) {
            request.setAttribute(ATTRIBUTE_CARD_MESSAGE, MESSAGE_NO_CARDS);
        } else {
            request.setAttribute(PARAMETER_USER_CARDS, usersCards);
        }
    }
}

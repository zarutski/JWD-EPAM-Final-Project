package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.UserData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.validation.UserValidation;
import by.epamtc.zarutski.service.FacilitiesService;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GoToUserDetailsCommand implements Command {

    private static final String USER_DETAILS_PAGE = "/WEB-INF/jsp/userDetails.jsp";
    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";
    private static final String GO_TO_SEARCH_USER = "controller?command=go_to_search_user";

    private static final String SESSION_AUTHENTICATION_DATA = "authentication_data";
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

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();
        FacilitiesService financeService = provider.getFacilitiesService();

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(SESSION_AUTHENTICATION_DATA);

        int userDetailsId = Integer.parseInt(request.getParameter(PARAMETER_USER_ID));
        String page = GO_TO_SEARCH_USER;

        // TODO --- сделать также для всех операций админа
        // TODO --- список
        // TODO GoToSearchUserCommand (ready)
        // TODO GoToUserDetailsCommand (ready)
        // TODO UnblockCardCommand (ready)
        //
        // TODO GoToCardOrder -- не для админа (фильтры)
        // TODO GoToPayments -- не для админа (фильтры)
        //
        // TODO --- добавить так, чтобы юзер не мог посмотреть деталей другого пользователя (проверка совпадения id)
        // TODO --- список
        // TODO controller?command=go_to_card_details&card_id=2 (ready)
        // TODO controller?command=go_to_account_details&account_id=2 (ready)
        // TODO controller?command=go_to_account_details&action=payment&account_id=2 (ready)
        // TODO controller?command=go_to_card_details&action=payment&card_id=2 (ready)

        UserData userData = null;
        List<Card> usersCards = null;
        List<Account> usersAccounts = null;

        try {
            usersCards = financeService.getUserCards(userDetailsId);
            setCardsAttribute(request, usersCards);

            usersAccounts = financeService.getAccounts(userDetailsId, DESTINATION_USER);
            setAccAttribute(request, usersAccounts);

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

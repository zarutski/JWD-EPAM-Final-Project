package by.epamtc.zarutski.controller.command.impl.go_to;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.FacilityService;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * The class {@code GoToCardDetailsCommand} implements navigation to the card's details page.
 * <p>
 * Requests data and forms a new request containing data about certain card,
 * connected accounts and operations performed for this card.
 *
 * @author Maksim Zarutski
 */
public class GoToCardDetailsCommand implements Command {

    private static final String CARD_DETAILS_PAGE = "/WEB-INF/jsp/cardDetails.jsp";
    private static final String CARD_PAYMENTS_PAGE = "/WEB-INF/jsp/cardTransfer.jsp";
    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";

    private static final String SESSION_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_USER_CARD_ID = "card_id";
    private static final String PARAMETER_CARD_ACCOUNTS = "card_accounts";
    private static final String REQ_PARAMETER_USER_ID = "user_id";

    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_NO_CARD = "no card";
    private static final String MESSAGE_NO_ACCOUNTS = "no_accounts";
    private static final String MESSAGE_NO_OPERATIONS = "no_operations";
    private static final String MESSAGE_SERVICE_ERROR = "&message=service_error";

    private static final String PARAMETER_USER_OPERATIONS = "operations_history";
    private static final String PARAMETER_USER_CARD = "card";

    private static final String PARAMETER_ACTION = "action";
    private static final String ACTION_PAYMENT = "payment";
    private static final String CARD_DESTINATION = "card";
    private static final String ROLE_ADMIN = "admin";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(SESSION_AUTHENTICATION_DATA);

        int cardId = Integer.parseInt(request.getParameter(PARAMETER_USER_CARD_ID));
        int userId = getUserId(authenticationData, request);
        List<Account> usersAccounts = null;
        Card card = null;

        String action = request.getParameter(PARAMETER_ACTION);
        String page = getActionPage(action);

        try {
            ServiceProvider provider = ServiceProvider.getInstance();
            FacilityService service = provider.getFacilityService();

            card = service.getCardById(cardId, userId);
            setCardAttribute(request, card);

            usersAccounts = service.getAccounts(cardId, CARD_DESTINATION);
            setAccAttribute(request, usersAccounts);

            if (page.equals(CARD_DETAILS_PAGE)) {
                List<Operation> operations = service.getOperations(cardId, CARD_DESTINATION);
                setOperationAttribute(request, operations);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } catch (WrongDataServiceException e) {
            response.sendRedirect(GO_TO_MAIN_PAGE);
        } catch (ServiceException e) {
            page += MESSAGE_SERVICE_ERROR;
            response.sendRedirect(page);
        }
    }

    /**
     * Method returns the id of the user for admin.
     * If authenticated user has user's role, method will return it's own id.
     * Thus, the user will not be able to obtain information about card
     * in the event that this card belongs to another person.
     *
     * @param authenticationData information obout authenticated user
     * @param request            request from user
     * @return user's id based on the authentication data
     */
    private int getUserId(AuthenticationData authenticationData, HttpServletRequest request) {
        if (authenticationData.getUserRole().equals(ROLE_ADMIN)) {
            String userIdParameter = request.getParameter(REQ_PARAMETER_USER_ID);
            return Integer.parseInt(userIdParameter);
        } else {
            return authenticationData.getUserId();
        }
    }

    /**
     * Get destination page based on action from user's request
     *
     * @param action parameter from user's request
     * @return target page that needs card details data
     */
    private String getActionPage(String action) {
        String page = null;
        if (ACTION_PAYMENT.equals(action)) {
            page = CARD_PAYMENTS_PAGE;
        } else {
            page = CARD_DETAILS_PAGE;
        }

        return page;
    }

    private void setAccAttribute(HttpServletRequest request, List<Account> usersAccounts) {
        if (usersAccounts == null || usersAccounts.isEmpty()) {
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_ACCOUNTS);
        } else {
            request.setAttribute(PARAMETER_CARD_ACCOUNTS, usersAccounts);
        }
    }

    private void setCardAttribute(HttpServletRequest request, Card card) {
        if (card == null) {
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_CARD);
        } else {
            request.setAttribute(PARAMETER_USER_CARD, card);
        }
    }

    private void setOperationAttribute(HttpServletRequest request, List<Operation> operations) {
        if (operations == null || operations.isEmpty()) {
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_OPERATIONS);
        } else {
            request.setAttribute(PARAMETER_USER_OPERATIONS, operations);
        }
    }
}
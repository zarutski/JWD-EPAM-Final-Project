package by.epamtc.zarutski.controller.command.impl.user;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.Card;
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

public class GoToCardsCommand implements Command {

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_USER_CARDS = "user_cards";

    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ERROR_SERVICE = "service_error";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_NO_CARDS = "no_cards";

    private static final String CARDS_PAGE = "/WEB-INF/jsp/cards.jsp";
    private static final String CARDS_LIST_PAGE = "/WEB-INF/jsp/cardsList.jsp";

    private static final String ACTION_PAYMENT = "payment";
    private static final String PARAMETER_ACTION = "action";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceProvider provider = ServiceProvider.getInstance();
        FacilityService service = provider.getFacilityService();

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

        int userId = authenticationData.getUserId();

        List<Card> usersCards = null;
        try {
            usersCards = service.getUserCards(userId);

            if (usersCards == null || usersCards.isEmpty()) {
                request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_CARDS);
            } else {
                request.setAttribute(PARAMETER_USER_CARDS, usersCards);
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
            page = CARDS_LIST_PAGE;
        } else {
            page = CARDS_PAGE;
        }

        return page;
    }
}
package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.CardService;
import by.epamtc.zarutski.service.ServiceProvider;
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

public class GoToCardsCommand implements Command {

    private static final Logger logger = LogManager.getLogger(GoToCardsCommand.class);

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_USER_CARDS = "user_cards";

    private static final String GO_TO_AUTHENTICATION_PAGE = "controller?command=go_to_authentication_page";
    private static final String CARDS_PAGE = "/WEB-INF/jsp/cards.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceProvider provider = ServiceProvider.getInstance();
        CardService service = provider.getCardService();

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

        if (authenticationData != null) {
            int userId = authenticationData.getUserId();

            List<Card> usersCards = null;
            try {
                usersCards = service.getUserCards(userId);

                if (usersCards == null) {
                    request.setAttribute("error", "no cards");
                } else {
                    request.setAttribute(PARAMETER_USER_CARDS, usersCards);
                }

            } catch (ServiceException e) {
                request.setAttribute("error", "другое сообщение");
            }
        } else {
            // TODO log
            response.sendRedirect(GO_TO_AUTHENTICATION_PAGE);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(CARDS_PAGE);
        dispatcher.forward(request, response);
    }
}
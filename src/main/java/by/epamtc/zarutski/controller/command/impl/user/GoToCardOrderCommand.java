package by.epamtc.zarutski.controller.command.impl.user;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToCardOrderCommand implements Command {

    private static final String ORDER_CARD_PAGE = "/WEB-INF/jsp/cardOrder.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ORDER_CARD_PAGE);
        dispatcher.forward(request, response);
    }
}
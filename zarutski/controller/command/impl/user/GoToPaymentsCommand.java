package by.epamtc.zarutski.controller.command.impl.user;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToPaymentsCommand implements Command {

	private static final String PAYMENTS_PAGE = "/WEB-INF/jsp/payments.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(PAYMENTS_PAGE);
        dispatcher.forward(request, response);
    }
}
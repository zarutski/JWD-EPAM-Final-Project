package by.epamtc.zarutski.controller.command.impl.go_to;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToAuthenticationCommand implements Command {

    private static final String AUTHENTICATION_PAGE = "/WEB-INF/jsp/authentication.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(AUTHENTICATION_PAGE);
        dispatcher.forward(request, response);
    }
}
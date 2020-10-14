package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToSearchUserCommand implements Command {

    private static final String SEARCH_USER_PAGE = "/WEB-INF/jsp/searchUser.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(SEARCH_USER_PAGE);
        dispatcher.forward(request, response);
    }
}

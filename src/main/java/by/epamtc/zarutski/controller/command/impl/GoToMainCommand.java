package by.epamtc.zarutski.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epamtc.zarutski.controller.command.Command;

public class GoToMainCommand implements Command {

    private static final String GO_TO_MAIN_PAGE = "/WEB-INF/jsp/main.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(GO_TO_MAIN_PAGE);
        dispatcher.forward(request, response);
    }
}
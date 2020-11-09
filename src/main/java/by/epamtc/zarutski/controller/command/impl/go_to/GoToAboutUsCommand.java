package by.epamtc.zarutski.controller.command.impl.go_to;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToAboutUsCommand implements Command {

    private static final String ABOUT_US_PAGE = "/WEB-INF/jsp/aboutUs.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ABOUT_US_PAGE);
        dispatcher.forward(request, response);
    }
}
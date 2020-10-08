package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class LogoutCommand implements Command {

    public static final String MAIN_PAGE = "controller?command=go_to_main_page";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(MAIN_PAGE);
    }
}
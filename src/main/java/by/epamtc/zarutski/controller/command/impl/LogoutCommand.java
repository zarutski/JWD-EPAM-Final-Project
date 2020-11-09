package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LogoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";
    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";

    private static final String LOG_LOGGED_OUT = "user logged out";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(PARAMETER_AUTHENTICATION_DATA);
            logger.info(LOG_LOGGED_OUT);
        }
        response.sendRedirect(GO_TO_MAIN_PAGE);
    }
}
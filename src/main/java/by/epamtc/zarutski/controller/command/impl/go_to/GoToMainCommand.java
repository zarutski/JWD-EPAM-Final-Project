package by.epamtc.zarutski.controller.command.impl.go_to;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;

/**
 * The class {@code GoToMainCommand} implements navigation to the main page.
 * <p>
 * For non-authenticated user main page is the main.jsp page.
 * For authenticated user main page is the personalArea.jsp page.
 *
 * @author Maksim Zarutski
 */
public class GoToMainCommand implements Command {

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";

    private static final String GO_TO_PERSONAL_AREA = "controller?command=go_to_personal_area";
    private static final String GO_TO_MAIN_PAGE = "/WEB-INF/jsp/main.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

        String page = GO_TO_MAIN_PAGE;
        if (authenticationData != null) {
            page = GO_TO_PERSONAL_AREA;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}
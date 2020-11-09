package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.validation.UserValidation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GoToSearchUserCommand implements Command {

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String SEARCH_USER_PAGE = "/WEB-INF/jsp/searchUser.jsp";
    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(SEARCH_USER_PAGE);
        dispatcher.forward(request, response);
    }
}
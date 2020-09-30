package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToRegistrationCommand implements Command {
	
	private static final String REGISTRATION_PAGE = "/WEB-INF/jsp/registration.jsp";
	
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher dispatcher = request.getRequestDispatcher(REGISTRATION_PAGE);
        dispatcher.forward(request, response);
    }
}

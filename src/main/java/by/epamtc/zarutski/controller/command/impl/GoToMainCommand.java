package by.epamtc.zarutski.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;

public class GoToMainCommand implements Command {

    private static final String GO_TO_MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
    private static final String GO_TO_PESONAL_AREA = "controller?command=go_to_personal_area";
    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	HttpSession session = request.getSession();
    	AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);
    	
    	String page = null;
    	if (authenticationData != null) {
    		page = GO_TO_PESONAL_AREA;
    	} else {
    		page = GO_TO_MAIN_PAGE;
    	}
    	 
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}
package by.epamtc.zarutski.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;

public class AuthenticationCommand implements Command{
	
	private static final String PARAMETER_LOGIN = "login";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";

    private static final String GO_TO_PESONAL_AREA = "controller?command=go_to_personal_area";
    private static final String DEFAULT_PAGE = "/index.jsp";
    private static final String AUTHENTICATION_PAGE = "controller?command=go_to_authentication_page";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
    	
        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();
        
        String login = request.getParameter(PARAMETER_LOGIN);
        String password = request.getParameter(PARAMETER_PASSWORD);

        AuthenticationData authenticationData = null;
        HttpSession session;
        String page;
                
        try {
            authenticationData = service.authentication(login, password);

            if (authenticationData == null) {
                request.setAttribute("error", "login or password error");
                page = AUTHENTICATION_PAGE;
            } else {
                session = request.getSession();
                session.setAttribute(PARAMETER_AUTHENTICATION_DATA, authenticationData);
                page = GO_TO_PESONAL_AREA;
            }

        } catch (WrongDataServiceException e) {
        	request.setAttribute("error", "login or password error");
            page = AUTHENTICATION_PAGE;
        } catch (ServiceException e) {
            // TODO --- + нужен лог (т.к. клиенту не нужны ошибки)
            request.setAttribute("error", "другое сообщение");
            page = DEFAULT_PAGE;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}
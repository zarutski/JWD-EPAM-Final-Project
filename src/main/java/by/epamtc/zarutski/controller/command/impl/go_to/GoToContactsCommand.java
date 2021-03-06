package by.epamtc.zarutski.controller.command.impl.go_to;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The class {@code GoToContactsCommand} implements navigation to contacts page
 *
 * @author Maksim Zarutski
 */
public class GoToContactsCommand implements Command {

    private static final String CONTACTS_PAGE = "/WEB-INF/jsp/contacts.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(CONTACTS_PAGE);
        dispatcher.forward(request, response);
    }
}
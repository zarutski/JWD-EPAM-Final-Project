package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * The class {@code LocalizationCommand} implements change of an app language
 * <p>
 * Gets parameter{@value PARAMETER_LOCALE} of target locale from request.
 * Puts locale parameter into {@code HttpSession} object as an attribute,
 * and redirects user to previous page.
 *
 * @author Maksim Zarutski
 */
public class LocalizationCommand implements Command {

    private final static String PARAMETER_LOCALE = "local";
    private final static String PARAMETER_PREVIOUS_COMMAND = "previous_command";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String previousCommand = (String) session.getAttribute(PARAMETER_PREVIOUS_COMMAND);
        String local = req.getParameter(PARAMETER_LOCALE);

        session.setAttribute(PARAMETER_LOCALE, local);
        resp.sendRedirect(previousCommand);
    }
}
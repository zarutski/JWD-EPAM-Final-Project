package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LocalizationCommand implements Command {

    private final static String PARAMETER_LOCAL = "local";
    private final static String PARAMETER_PREVIOUS_COMMAND = "previous_command";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String local = req.getParameter(PARAMETER_LOCAL);
        String previousCommand = req.getParameter(PARAMETER_PREVIOUS_COMMAND);
        req.getSession().setAttribute(PARAMETER_LOCAL, local);
        resp.sendRedirect("controller?command=" + previousCommand);
    }
}
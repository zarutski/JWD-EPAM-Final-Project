package by.epamtc.zarutski.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.command.CommandProvider;
import by.epamtc.zarutski.controller.command.ParameterName;

public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CommandProvider provider = new CommandProvider();

    private static final String PARAMETER_COMMAND_NAME = "command";
    private static final String PARAMETER_CONTROLLER = "controller";
    private final static String PARAMETER_PREVIOUS_COMMAND = "previous_command";
    private final static String QUESTION_MARK = "?";

    public Controller() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String currentCommandName = req.getParameter(PARAMETER_COMMAND_NAME);
        Command command = null;

        if (currentCommandName != null) {

            try {
                command = provider.getCommand(currentCommandName);
            } catch (IllegalArgumentException e) {
                String mainPageCommand = ParameterName.GO_TO_MAIN_PAGE.toString();
                command = provider.getCommand(mainPageCommand);
            }

            if (command != null) {
                command.execute(req, resp);

                String query = req.getQueryString();
                String previousCommand = PARAMETER_CONTROLLER + QUESTION_MARK + query;
                session.setAttribute(PARAMETER_PREVIOUS_COMMAND, previousCommand);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
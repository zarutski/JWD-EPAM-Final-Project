package by.epamtc.zarutski.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.command.CommandProvider;

public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CommandProvider provider = new CommandProvider();
    private static final String COMMAND_NAME = "command";

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
        String currentCommandName = req.getParameter(COMMAND_NAME);
        Command command = provider.getCommand(currentCommandName);
        command.execute(req, resp);
    }
}
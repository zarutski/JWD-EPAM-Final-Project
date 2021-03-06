package by.epamtc.zarutski.controller.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface {@code Command} for implementation command
 *
 * @author Maksim Zarutski
 */
public interface Command {

    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
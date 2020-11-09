package by.epamtc.zarutski.controller.filter;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.ParameterName;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class AuthorizationFilter implements Filter {

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_COMMAND_NAME = "command";

    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";

    private static final String ADMIN = "admin";
    private static final String USER = "user";

    private static final Set<ParameterName> guestCommands = EnumSet.of(
            ParameterName.AUTHENTICATION,
            ParameterName.GO_TO_REGISTRATION_PAGE,
            ParameterName.GO_TO_AUTHENTICATION_PAGE
    );
    private static final Set<ParameterName> userCommands = EnumSet.of(
            ParameterName.GO_TO_PERSONAL_AREA,
            ParameterName.USER_PHOTO_UPLOAD,
            ParameterName.UPDATE_USER_DATA,
            ParameterName.GO_TO_CARD_DETAILS,
            ParameterName.BLOCK_CARD,
            ParameterName.GO_TO_ACCOUNT_DETAILS
    );
    private static final Set<ParameterName> onlyUserCommands = EnumSet.of(
            ParameterName.GO_TO_CARDS,
            ParameterName.GO_TO_CARD_ORDER,
            ParameterName.ORDER_CARD,
            ParameterName.GO_TO_PAYMENTS,
            ParameterName.TRANSFER,
            ParameterName.GO_TO_ACCOUNTS
    );
    private static final Set<ParameterName> onlyAdminCommands = EnumSet.of(
            ParameterName.GO_TO_SEARCH_USER,
            ParameterName.SEARCH_USER,
            ParameterName.GO_TO_USER_DETAILS,
            ParameterName.CHANGE_USER_ROLE,
            ParameterName.UNBLOCK_CARD
    );


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String commandName = req.getParameter(PARAMETER_COMMAND_NAME);
        ParameterName parameterName = null;

        try {
            if (commandName != null) {
                commandName = commandName.toUpperCase();
                parameterName = ParameterName.valueOf(commandName);
            }
        } catch (IllegalArgumentException ignore) {
            parameterName = ParameterName.GO_TO_MAIN_PAGE;
        }

        if (parameterName != null) {

            HttpSession session = req.getSession();
            AuthenticationData authentication = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

            if (guestCommands.contains(parameterName) && authentication != null) {
                // user is authenticated, but trying to access authentication or registration pages
                resp.sendRedirect(GO_TO_MAIN_PAGE);
            } else if (userCommands.contains(parameterName) && authentication == null) {
                // user is not authenticated
                resp.sendRedirect(GO_TO_MAIN_PAGE);
            } else if (onlyUserCommands.contains(parameterName) && (isAuthenticated(authentication, ADMIN))) {
                // admin can't access command
                resp.sendRedirect(GO_TO_MAIN_PAGE);
            } else if (onlyAdminCommands.contains(parameterName) && (isAuthenticated(authentication, USER))) {
                // user can't access command
                resp.sendRedirect(GO_TO_MAIN_PAGE);
            } else {
                chain.doFilter(req, resp);
            }

        } else {
            // proceeding filter's chain, when there's no command Parameter
            chain.doFilter(req, resp);
        }
    }

    private boolean isAuthenticated(AuthenticationData data, String role) {
        return data == null || role.equals(data.getUserRole());
    }
}

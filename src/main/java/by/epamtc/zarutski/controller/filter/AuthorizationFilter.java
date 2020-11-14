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

/**
 * The {@code AuthorizationFilter} class validates user authentication data {@link AuthenticationData}
 * to provide access to specific parts of the application.
 *
 * @author Maksim Zarutski
 * @see Set
 */
public class AuthorizationFilter implements Filter {

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_COMMAND_NAME = "command";

    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";

    private static final String ADMIN = "admin";
    private static final String USER = "user";

    /**
     * {@code Set} object stores command names {@link ParameterName} available to a non-authenticated user
     */
    private static final Set<ParameterName> guestCommands = EnumSet.of(
            ParameterName.AUTHENTICATION,
            ParameterName.GO_TO_REGISTRATION_PAGE,
            ParameterName.GO_TO_AUTHENTICATION_PAGE
    );

    /**
     * {@code Set} object stores command names {@code ParameterName}
     * available to the authenticated user whatever its role
     */
    private static final Set<ParameterName> userCommands = EnumSet.of(
            ParameterName.GO_TO_PERSONAL_AREA,
            ParameterName.USER_PHOTO_UPLOAD,
            ParameterName.UPDATE_USER_DATA,
            ParameterName.GO_TO_CARD_DETAILS,
            ParameterName.BLOCK_CARD,
            ParameterName.GO_TO_ACCOUNT_DETAILS
    );
    /**
     * {@code Set} object stores command names {@code ParameterName}
     * available only to the authenticated user with role {@value USER}
     */
    private static final Set<ParameterName> onlyUserCommands = EnumSet.of(
            ParameterName.GO_TO_CARDS,
            ParameterName.GO_TO_CARD_ORDER,
            ParameterName.ORDER_CARD,
            ParameterName.GO_TO_PAYMENTS,
            ParameterName.TRANSFER,
            ParameterName.GO_TO_ACCOUNTS
    );
    /**
     * {@code Set} object stores command names {@code ParameterName}
     * available only to the authenticated user with role {@value ADMIN}
     */
    private static final Set<ParameterName> onlyAdminCommands = EnumSet.of(
            ParameterName.GO_TO_SEARCH_USER,
            ParameterName.SEARCH_USER,
            ParameterName.GO_TO_USER_DETAILS,
            ParameterName.CHANGE_USER_ROLE,
            ParameterName.UNBLOCK_CARD
    );

    /**
     * Checks if requested command can be executed by the client.
     * <p>
     * If client has no access to the requested command sends redirect to the main page {@value GO_TO_MAIN_PAGE}
     *
     * @param request  the {@code ServletRequest} object contains client's request
     * @param response the {@code ServletResponse} object contains filter's response
     * @param chain    the {@code FilterChain} object is used to invoke next filter in chain
     * @throws IOException      if an IO error occurs during performing IO operations
     * @throws ServletException if an exception occurs during filter's work
     */
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
        } catch (IllegalArgumentException e) {
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

    /**
     * Checks if user is allowed to perform certain command
     *
     * @param data is an {@code AuthenticationData} object that stores information about user's authentication
     * @param role of the user, that should be matching the role of the authenticated user
     * @return true - if user is authenticated and it's role matches to the necessary role
     */
    private boolean isAuthenticated(AuthenticationData data, String role) {
        return data == null || role.equals(data.getUserRole());
    }
}
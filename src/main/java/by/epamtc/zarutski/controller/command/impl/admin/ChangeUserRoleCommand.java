package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeUserRoleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ChangeUserRoleCommand.class);

    private static final String PARAMETER_USER_ID = "user_id";
    private static final String PARAMETER_USER_ROLE = "user_role";

    private static final String PARAMETER_SERVICE_ERROR = "&message=service_error";
    private static final String PARAMETER_WRONG_DATA = "&message=wrong_data";

    private static final String GO_TO_USER_DETAILS = "controller?command=go_to_user_details&user_id=";
    private static final String LOG_SUCCESSFUL_START = "user ";
    private static final String LOG_SUCCESSFUL_END = "(id) set role: ";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userIdParameter = request.getParameter(PARAMETER_USER_ID);
        String roleCodeParameter = request.getParameter(PARAMETER_USER_ROLE);
        int userId = Integer.parseInt(userIdParameter);
        int roleCode = Integer.parseInt(roleCodeParameter);

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();
        String page = GO_TO_USER_DETAILS + userId;

        try {
            service.changeUserRole(userId, roleCode);
            logger.info(LOG_SUCCESSFUL_START + userId + LOG_SUCCESSFUL_END + roleCode);
        } catch (WrongDataServiceException e) {
            page += PARAMETER_WRONG_DATA;
        } catch (ServiceException e) {
            page += PARAMETER_SERVICE_ERROR;
        }

        response.sendRedirect(page);
    }
}
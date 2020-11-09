package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.command.impl.BlockCardCommand;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeUserRoleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(BlockCardCommand.class);

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    public static final String PARAMETER_USER_ID = "user_id";
    public static final String PARAMETER_USER_ROLE = "user_role";

    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";
    private static final String GO_TO_USER_DETAILS = "controller?command=go_to_user_details&user_id=";

    private static final String PARAMETER_SERVICE_ERROR = "&message=service_error";
    private static final String PARAMETER_WRONG_DATA = "&message=wrong_data";

    public static final String LOG_SERVICE_ERROR = "service error";
    private static final String LOG_SUCCESSFUL_START = "user ";
    private static final String LOG_SUCCESSFUL_END = "(id) set role: ";

    public static final String ROLE_ADMIN = "admin";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);
        String page = GO_TO_MAIN_PAGE;

        String userIdParameter = request.getParameter(PARAMETER_USER_ID);
        int userId = Integer.parseInt(userIdParameter);

        String roleCodeParameter = request.getParameter(PARAMETER_USER_ROLE);
        int roleCode = Integer.parseInt(roleCodeParameter);

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();
        page = GO_TO_USER_DETAILS + userId;

        try {
            service.changeUserRole(userId, roleCode);
            logger.info(LOG_SUCCESSFUL_START + userId + LOG_SUCCESSFUL_END + roleCode);
        } catch (WrongDataServiceException e) {
            page += PARAMETER_WRONG_DATA; // TODO --- логировать тут, после валидации
        } catch (ServiceException e) {
            logger.info(LOG_SERVICE_ERROR, e); // TODO --- если ex логируется раньше, то не стоит повторно логировать
            page += PARAMETER_SERVICE_ERROR;
        }

        response.sendRedirect(page);
    }
}

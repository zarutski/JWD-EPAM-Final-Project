package by.epamtc.zarutski.controller.command.impl.admin;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.FacilitiesActionService;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class UnblockCardCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UnblockCardCommand.class);

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    public static final String PARAMETER_CARD_ID = "card_id";
    private static final String PARAMETER_CARD_STATE = "card_state";

    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";
    private static final String GO_TO_CARD_DETAILS = "controller?command=go_to_card_details&card_id=";

    private static final String PARAMETER_SERVICE_ERROR = "&message=service_error";
    private static final String PARAMETER_WRONG_DATA = "&message=wrong_data";

    public static final String LOG_SERVICE_ERROR = "service error";
    private static final String LOG_SUCCESSFUL_START = "card ";
    private static final String LOG_SUCCESSFUL_END = "(id) was unblocked successfully";

    public static final String PARAMETER_USER_ID_NAME = "user_id";
    public static final String PARAMETER_USER_ID_QUERY = "&user_id=";

    public static final String ROLE_ADMIN = "admin";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);
        String page = GO_TO_MAIN_PAGE;

        String cardIdParameter = request.getParameter(PARAMETER_CARD_ID);
        int cardId = Integer.parseInt(cardIdParameter);

        String cardStateParameter = request.getParameter(PARAMETER_CARD_STATE);
        int cardState = Integer.parseInt(cardStateParameter);


        ServiceProvider provider = ServiceProvider.getInstance();
        FacilitiesActionService service = provider.getFacilitiesActionService();

        String parameterUserId = request.getParameter(PARAMETER_USER_ID_NAME);
        page = GO_TO_CARD_DETAILS + cardIdParameter + PARAMETER_USER_ID_QUERY + parameterUserId;

        try {
            service.changeCardState(cardId, cardState);
            logger.info(LOG_SUCCESSFUL_START + cardId + LOG_SUCCESSFUL_END);
        } catch (WrongDataServiceException e) {
            page += PARAMETER_WRONG_DATA;
        } catch (ServiceException e) {
            logger.info(LOG_SERVICE_ERROR, e);
            page += PARAMETER_SERVICE_ERROR;
        }

        response.sendRedirect(page);
    }
}
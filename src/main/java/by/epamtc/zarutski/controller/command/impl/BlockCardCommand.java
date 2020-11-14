package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.FacilityActionService;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The class {@code BlockCardCommand} implements command to block user's card.
 * <p>
 * Uses user id and new card state from request in order to change card state.
 *
 * @author Maksim Zarutski
 */
public class BlockCardCommand implements Command {

    private static final Logger logger = LogManager.getLogger(BlockCardCommand.class);

    private static final String PARAMETER_USER_ID_NAME = "user_id";
    private static final String PARAMETER_CARD_ID = "card_id";
    private static final String PARAMETER_CARD_STATE = "card_state";

    private static final String PARAMETER_SERVICE_ERROR = "&message=service_error";
    private static final String PARAMETER_WRONG_DATA = "&message=wrong_data";
    private static final String PARAMETER_USER_ID = "&user_id=";

    private static final String GO_TO_CARD_DETAILS = "controller?command=go_to_card_details&card_id=";

    private static final String LOG_SUCCESSFUL_START = "card ";
    private static final String LOG_SUCCESSFUL_END = "(id) was blocked successfully";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cardIdParameter = request.getParameter(PARAMETER_CARD_ID);
        String cardStateParameter = request.getParameter(PARAMETER_CARD_STATE);
        int cardId = Integer.parseInt(cardIdParameter);
        int cardState = Integer.parseInt(cardStateParameter);

        String page = GO_TO_CARD_DETAILS;
        String parameterUserId = request.getParameter(PARAMETER_USER_ID_NAME);
        page += cardIdParameter + PARAMETER_USER_ID + parameterUserId;

        try {
            ServiceProvider provider = ServiceProvider.getInstance();
            FacilityActionService service = provider.getFacilityActionService();

            service.changeCardState(cardId, cardState);
            logger.info(LOG_SUCCESSFUL_START + cardId + LOG_SUCCESSFUL_END);
        } catch (WrongDataServiceException e) {
            page += PARAMETER_WRONG_DATA;
        } catch (ServiceException e) {
            page += PARAMETER_SERVICE_ERROR;
        }

        response.sendRedirect(page);
    }
}
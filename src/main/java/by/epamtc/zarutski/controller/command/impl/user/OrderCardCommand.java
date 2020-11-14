package by.epamtc.zarutski.controller.command.impl.user;

import by.epamtc.zarutski.bean.*;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

/**
 * The class {@code OrderCardCommand} implements command for ordering a new card for the user.
 * <p>
 * Forms {@code CardOrderData} and {@code AccOrderData} objects based on request parameters to order a new card.
 *
 * @author Maksim Zarutski
 */
public class OrderCardCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderCardCommand.class);

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_CARD_ORDER_SUCCESS = "&message=success";
    private static final String PARAMETER_ORDER_DATA_ERROR = "&message=data_error";
    private static final String PARAMETER_SERVICE_ERROR = "&message=service_error";

    private static final String PARAMETER_OWNER = "owner";
    private static final String PARAMETER_PAYMENT_SYSTEM = "payment_system";
    private static final String PARAMETER_VALIDITY = "validity";
    private static final String PARAMETER_CURRENCY = "currency";

    private static final String GO_TO_CARD_ORDER_PAGE = "controller?command=go_to_card_order";
    private static final String LOG_WRONG_ORDER_DATA = "Card order data isn't correct";

    private static final String ACC_NUMBER_STUB = "111122223333444455556666";
    private static final String CARD_NUMBER_STUB = "1111222233334444";
    private static final String CVV_CODE_STUB = "000";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);

        String owner = request.getParameter(PARAMETER_OWNER);
        String paymentSystem = request.getParameter(PARAMETER_PAYMENT_SYSTEM);
        String validity = request.getParameter(PARAMETER_VALIDITY);
        String currency = request.getParameter(PARAMETER_CURRENCY);
        Date cardExpirationDate = getExpirationDate(validity);

        CardOrderData cardData = new CardOrderData();
        cardData.setCardNumber(CARD_NUMBER_STUB);
        cardData.setExpirationDate(cardExpirationDate);
        cardData.setOwner(owner);
        cardData.setCvvCode(CVV_CODE_STUB);
        cardData.setPaymentSystem(paymentSystem);

        int userId = authenticationData.getUserId();
        int currencyCode = Integer.parseInt(currency);
        Date openingDate = getCurrentDate();

        AccOrderData accData = new AccOrderData();
        accData.setUserId(userId);
        accData.setAccNumber(ACC_NUMBER_STUB);
        accData.setOpeningDate(openingDate);
        accData.setCurrencyCode(currencyCode);

        String page = GO_TO_CARD_ORDER_PAGE;

        try {
            ServiceProvider provider = ServiceProvider.getInstance();
            FacilityActionService service = provider.getFacilityActionService();

            if (service.orderNewCard(cardData, accData)) {
                page += PARAMETER_CARD_ORDER_SUCCESS;
            } else {
                page += PARAMETER_SERVICE_ERROR;
            }
        } catch (WrongDataServiceException e) {
            logger.info(LOG_WRONG_ORDER_DATA, e);
            page += PARAMETER_ORDER_DATA_ERROR;
        } catch (ServiceException e) {
            page += PARAMETER_SERVICE_ERROR;
        }

        response.sendRedirect(page);
    }

    private Date getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        return Date.valueOf(localDate);
    }

    /**
     * Forms {@code java.sql.Date} object representing card expiration date based on provided validity
     *
     * @param validity of the new card in months
     * @return {@code java.sql.Date} card expiration date
     */
    private Date getExpirationDate(String validity) {
        long validityMonth = Long.parseLong(validity);

        LocalDate localDate = LocalDate.now();
        LocalDate expirationDate = localDate.plusMonths(validityMonth);

        return Date.valueOf(expirationDate);
    }
}
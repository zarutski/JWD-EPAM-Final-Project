package by.epamtc.zarutski.controller.command.impl.user;

import by.epamtc.zarutski.bean.TransferData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.validation.TransferDataValidator;
import by.epamtc.zarutski.service.FacilitiesActionService;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.exception.InsufficientFundsServiceException;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO --- rename OR split
public class TransferCommand implements Command {

    private static final Logger logger = LogManager.getLogger(TransferCommand.class);

    private static final String PARAMETER_PAYMENT_SUCCESSFUL = "&message=successful";
    private static final String PARAMETER_CURRENCY_MATCH = "&message=match_currency";
    public static final String PARAMETER_INPUT_DATA_FORMAT = "&message=input_data";
    public static final String PARAMETER_INSUFFICIENT_FUNDS = "&message=insufficient_funds";
    private static final String PARAMETER_SERVICE_ERROR = "&message=service_error";

    private static final String GO_TO_ACCOUNT_DETAILS = "controller?command=go_to_account_details" +
            "&action=payment&account_id=";

    private static final String GO_TO_CARD_DETAILS = "controller?command=go_to_card_details" +
            "&action=payment&card_id=";

    public static final String ACCOUNT_ID = "account_id";
    public static final String TRANSFER_FROM_CARD = "card";
    public static final String LOG_TRANSFER_SUCCESSFUL = "Transfer committed successful";
    public static final String LOG_WRONG_TRANSFER_DATA = "Transfer data format isn't correct";
    public static final String PARAMETER_ACCOUNT_AMOUNT = "account_amount";
    public static final String PARAMETER_TRANSFER_AMOUNT = "transfer_amount";
    public static final String PARAMETER_PAYMENT_CURRENCY = "payment_currency";
    public static final String PARAMETER_ACCOUNT_NUMBER = "account_number";
    public static final String PARAMETER_DESTINATION_NUMBER = "destination_number";
    public static final String PARAMETER_TRANSFER_FROM = "transfer_from";
    public static final String PARAMETER_CARD_ID = "card_id";
    public static final String PARAMETER_CARD_NUMBER = "card_number";
    public static final String PARAMETER_CONFIRMATION_CODE = "confirmation_code";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TransferData transferData = formTransferData(request);
        addSenderCardData(transferData, request);
        String page = getDestinationPage(transferData);

        if (TransferDataValidator.isDataExists(transferData)) {

            ServiceProvider provider = ServiceProvider.getInstance();
            FacilitiesActionService service = provider.getFacilitiesActionService();

            try {
                if (service.transfer(transferData)) {
                    logger.info(LOG_TRANSFER_SUCCESSFUL);
                    page += PARAMETER_PAYMENT_SUCCESSFUL;
                } else {
                    page += PARAMETER_CURRENCY_MATCH;
                }
            } catch (WrongDataServiceException e) {
                page += PARAMETER_INPUT_DATA_FORMAT;
            } catch (InsufficientFundsServiceException e) {
                page += PARAMETER_INSUFFICIENT_FUNDS;
            } catch (ServiceException e) {
                page += PARAMETER_SERVICE_ERROR;
            }
        } else {
            logger.info(LOG_WRONG_TRANSFER_DATA);
            page += PARAMETER_INPUT_DATA_FORMAT;
        }

        response.sendRedirect(page);
    }

    private TransferData formTransferData(HttpServletRequest request) {
        TransferData transferData = new TransferData();

        int accountId = Integer.parseInt(request.getParameter(ACCOUNT_ID));
        long senderAccAmount = getDBFormatAmount(request, PARAMETER_ACCOUNT_AMOUNT);
        long transferAmount = getDBFormatAmount(request, PARAMETER_TRANSFER_AMOUNT);
        String currency = request.getParameter(PARAMETER_PAYMENT_CURRENCY);
        String senderAccNumber = request.getParameter(PARAMETER_ACCOUNT_NUMBER);
        String destinationNumber = request.getParameter(PARAMETER_DESTINATION_NUMBER);
        String transferFrom = request.getParameter(PARAMETER_TRANSFER_FROM);

        transferData.setSenderAccountId(accountId);
        transferData.setSenderAccAmount(senderAccAmount);
        transferData.setTransferAmount(transferAmount);
        transferData.setTransferCurrency(currency);
        transferData.setSenderAccNumber(senderAccNumber);
        transferData.setDestinationNumber(destinationNumber);
        transferData.setTransferFrom(transferFrom);

        return transferData;
    }

    private long getDBFormatAmount(HttpServletRequest request, String fieldName) {
        double amountInput = Double.parseDouble(request.getParameter(fieldName));
        amountInput = amountInput * 100;
        return (long) amountInput;
    }

    private void addSenderCardData(TransferData transferData, HttpServletRequest request) {
        String transferFrom = transferData.getTransferFrom();

        if (transferFrom.equals(TRANSFER_FROM_CARD)) {
            int senderCardId = Integer.parseInt(request.getParameter(PARAMETER_CARD_ID));
            String senderCardNumber = request.getParameter(PARAMETER_CARD_NUMBER);
            String confirmationCode = request.getParameter(PARAMETER_CONFIRMATION_CODE);

            transferData.setSenderCardId(senderCardId);
            transferData.setSenderCardNumber(senderCardNumber);
            transferData.setConfirmationCode(confirmationCode);
        }
    }

    private String getDestinationPage(TransferData transferData) {
        String transferFrom = transferData.getTransferFrom();
        int senderCardId = transferData.getSenderCardId();
        int senderAccountId = transferData.getSenderAccountId();

        if (transferFrom.equals(TRANSFER_FROM_CARD)) {
            return GO_TO_CARD_DETAILS + senderCardId;
        } else {
            return GO_TO_ACCOUNT_DETAILS + senderAccountId;
        }
    }
}
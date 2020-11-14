package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.TransferData;

/**
 * The class {@code OperationValidator} validates the {@code TransferData} object's data
 *
 * @author Maksim Zarutski
 */
public class OperationValidator {

    private static final String ACC_PATTERN = "^([a-zA-z]|[0-9]){24}$";
    private static final String CARD_PATTERN = "^[0-9]{16}$";
    private static final String CURRENCY_PATTERN = "^[a-zA-z]{3}$";
    private static final String CONFIRMATION_CODE_PATTERN = "^[0-9]{3}$";
    private static final String TRANSFER_FROM_CARD = "card";
    public static final String CARD_STATE_BLOCKED = "заблокирована";

    /**
     * Checks if {@code TransferData} object's data is valid
     * <p>
     * Returns false if transfer's amount is bigger than account's available funds
     * <p>
     * Validates possibility of the operation when making
     * a transfer from card to card, or from account to account
     *
     * @param transferData containing data for performing transfer
     * @return boolean value indicating that transfer data is valid
     */
    public static boolean transferDataValidation(TransferData transferData) {
        String transferFrom = transferData.getTransferFrom();

        long senderAccAmount = transferData.getSenderAccAmount();
        long transferAmount = transferData.getTransferAmount();
        if (transferAmount > senderAccAmount) {
            return false;
        }

        if (transferFrom.equals(TRANSFER_FROM_CARD)) {
            return validateCardTransfer(transferData);
        } else {
            return validateAccTransfer(transferData);
        }
    }

    /**
     * Validates possibility of the operation when making a transfer from card to card
     * <p>
     * Returns false value if sender's and destination numbers are matching
     * Returns false value if sender's card is blocked
     * Returns true if certain fields are matching certain patterns
     *
     * @param transferData containing data for performing transfer
     * @return boolean value indicating that transfer data is valid
     */
    private static boolean validateCardTransfer(TransferData transferData) {
        String currency = transferData.getTransferCurrency();
        String accNumber = transferData.getSenderAccNumber();
        String senderCardNumber = transferData.getSenderCardNumber();
        String destinationNumber = transferData.getDestinationNumber();
        String confirmationCode = transferData.getConfirmationCode();
        String cardState = transferData.getCardState();

        if (senderCardNumber.equals(destinationNumber) || CARD_STATE_BLOCKED.equals(cardState)) {
            return false;
        }

        return accNumber.matches(ACC_PATTERN) && destinationNumber.matches(CARD_PATTERN) &&
                senderCardNumber.matches(CARD_PATTERN) && currency.matches(CURRENCY_PATTERN)
                && confirmationCode.matches(CONFIRMATION_CODE_PATTERN);
    }

    /**
     * Validates possibility of the operation when making a transfer from account to account
     * <p>
     * Returns false value if sender's and destination numbers are matching
     * Returns true if certain fields are matching certain patterns
     *
     * @param transferData containing data for performing transfer
     * @return boolean value indicating that transfer data is valid
     */
    private static boolean validateAccTransfer(TransferData transferData) {
        String accNumber = transferData.getSenderAccNumber();
        String destinationNumber = transferData.getDestinationNumber();
        if (accNumber.equals(destinationNumber)) {
            return false;
        }

        String currency = transferData.getTransferCurrency();
        return accNumber.matches(ACC_PATTERN) && destinationNumber.matches(ACC_PATTERN) &&
                currency.matches(CURRENCY_PATTERN);
    }
}
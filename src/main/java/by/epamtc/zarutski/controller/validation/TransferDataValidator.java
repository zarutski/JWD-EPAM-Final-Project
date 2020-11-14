package by.epamtc.zarutski.controller.validation;

import by.epamtc.zarutski.bean.TransferData;

/**
 * The class {@code TransferDataValidator} validates the {@code TransferData} object's data
 *
 * @author Maksim Zarutski
 */
public class TransferDataValidator {

    private static final String TRANSFER_FROM_CARD = "card";
    private static final long UNSUPPORTED_AMOUNT_VALUE = 0L;
    private static final int UNSUPPORTED_ID_VALUE = 0;

    public static boolean isDataExists(TransferData data) {
        if (data == null) {
            return false;
        }

        int accountId = data.getSenderAccountId();
        long accAmount = data.getSenderAccAmount();
        long transferAmount = data.getTransferAmount();
        String currency = data.getTransferCurrency();
        String senderAccNumber = data.getSenderAccNumber();
        String destinationNumber = data.getDestinationNumber();
        String transferFrom = data.getTransferFrom();

        return accountId > UNSUPPORTED_ID_VALUE
                && accAmount > UNSUPPORTED_AMOUNT_VALUE
                && transferAmount > UNSUPPORTED_AMOUNT_VALUE
                && notNullOrEmpty(currency, senderAccNumber, destinationNumber, transferFrom)
                && correctCardData(data, transferFrom);
    }

    /**
     * Checks is {@code TransferData} object's data is correct
     * <p>
     * If transfer is provided from card, method will check card date from {@code TransferData} objet
     * Otherwise, method will return true
     *
     * @param data         is a {@code TransferData} object, contains data to transfer operation
     * @param transferFrom is a string to define a target of the transfer
     * @return boolean value indicates that transfer data is correct
     */
    private static boolean correctCardData(TransferData data, String transferFrom) {
        if (!TRANSFER_FROM_CARD.equals(transferFrom)) {
            return true;
        }

        int cardId = data.getSenderCardId();
        String cardNumber = data.getSenderCardNumber();
        String confirmationCode = data.getConfirmationCode();

        return (cardId > 0) && notNullOrEmpty(cardNumber, confirmationCode);
    }


    private static boolean notNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
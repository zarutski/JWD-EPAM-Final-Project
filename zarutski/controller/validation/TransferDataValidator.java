package by.epamtc.zarutski.controller.validation;

import by.epamtc.zarutski.bean.TransferData;

public class TransferDataValidator {

    public static final String TRANSFER_FROM_CARD = "card";
    public static final long UNSUPPORTED_AMOUNT_VALUE = 0L;
    public static final int UNSUPPORTED_ID_VALUE = 0;

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
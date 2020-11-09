package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.TransferData;

public class OperationValidator {

    private static final String ACC_PATTERN = "^([a-zA-z]|[0-9]){24}$";
    private static final String CARD_PATTERN = "^[0-9]{16}$";
    private static final String CURRENCY_PATTERN = "^[a-zA-z]{3}$";
    private static final String CONFIRMATION_CODE_PATTERN = "^[0-9]{3}$";
    private static final String TRANSFER_FROM_CARD = "card";
    public static final String CARD_STATE_BLOCKED = "заблокирована";

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
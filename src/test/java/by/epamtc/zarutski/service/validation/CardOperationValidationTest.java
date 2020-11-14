package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.TransferData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardOperationValidationTest {

    private TransferData cardTransfer;

    private static final int SENDER_ACCOUNT_ID = 1;
    private static final int SENDER_ACC_AMOUNT = 1000;
    private static final String SENDER_ACC_NUMBER = "BY10UNBS3135301412345678";
    private static final int TRANSFER_AMOUNT = 500;
    private static final String TRANSFER_CURRENCY = "BYN";
    private static final String DESTINATION_CARD_NUMBER = "5185553343850289";
    private static final String TRANSFER_FROM_CARD = "card";
    private static final int SENDER_CARD_ID = 1;
    private static final String SENDER_CARD_NUMBER = "4539804056541904";
    private static final String CONFIRMATION_CODE = "332";
    private static final String CARD_STATE = "активна";
    private static final int WRONG_TRANSFER_AMOUNT = 1001;
    private static final String CARD_STATE_BLOCKED = "заблокирована";
    private static final String INVALID_ACC_NUMBER = "by300200100";
    private static final String INVALID_CARD_NUMBER = "0000 0000";
    private static final String INVALID_CURRENCY_PATTERN = "byn belarus";
    private static final String INVALID_CVV_PATTERN = "0000";

    @Before
    public void setup() {
        cardTransfer = new TransferData();
        cardTransfer.setSenderAccountId(SENDER_ACCOUNT_ID);
        cardTransfer.setSenderAccAmount(SENDER_ACC_AMOUNT);
        cardTransfer.setSenderAccNumber(SENDER_ACC_NUMBER);
        cardTransfer.setTransferAmount(TRANSFER_AMOUNT);
        cardTransfer.setTransferCurrency(TRANSFER_CURRENCY);
        cardTransfer.setDestinationNumber(DESTINATION_CARD_NUMBER);
        cardTransfer.setTransferFrom(TRANSFER_FROM_CARD);
        cardTransfer.setSenderCardId(SENDER_CARD_ID);
        cardTransfer.setSenderCardNumber(SENDER_CARD_NUMBER);
        cardTransfer.setConfirmationCode(CONFIRMATION_CODE);
        cardTransfer.setCardState(CARD_STATE);
    }

    @Test
    public void cardTransferPositive() {
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertTrue(actual);
    }

    @Test
    public void cardTransferNegativeInsufficientFunds() {
        cardTransfer.setTransferAmount(WRONG_TRANSFER_AMOUNT);
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeWrongDestination() {
        String senderNumber = cardTransfer.getSenderCardNumber();
        cardTransfer.setDestinationNumber(senderNumber);
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeCardBlocked() {
        cardTransfer.setCardState(CARD_STATE_BLOCKED);
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeAccPattern() {
        cardTransfer.setSenderAccNumber(INVALID_ACC_NUMBER);
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeCardPattern() {
        cardTransfer.setSenderCardNumber(INVALID_CARD_NUMBER);
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeCurrencyPattern() {
        cardTransfer.setTransferCurrency(INVALID_CURRENCY_PATTERN);
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeCvvPattern() {
        cardTransfer.setConfirmationCode(INVALID_CVV_PATTERN);
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

}
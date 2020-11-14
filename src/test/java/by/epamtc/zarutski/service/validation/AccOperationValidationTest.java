package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.TransferData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccOperationValidationTest {

    private TransferData accTransfer;

    private static final int SENDER_ACCOUNT_ID = 1;
    private static final int SENDER_ACC_AMOUNT = 1000;
    private static final String SENDER_ACC_NUMBER = "BY10UNBS3135301412345678";
    private static final int TRANSFER_AMOUNT = 500;
    private static final String TRANSFER_CURRENCY = "BYN";
    private static final String DESTINATION_ACC_NUMBER = "BY12UNBS3078301434561278";
    private static final String TRANSFER_FROM_ACC = "acc";
    private static final int WRONG_TRANSFER_AMOUNT = 1001;
    private static final String INVALID_ACC_NUMBER = "by300200100";
    private static final String INVALID_CURRENCY_PATTERN = "byn belarus";

    @Before
    public void setup() {
        accTransfer = new TransferData();
        accTransfer.setSenderAccountId(SENDER_ACCOUNT_ID);
        accTransfer.setSenderAccAmount(SENDER_ACC_AMOUNT);
        accTransfer.setSenderAccNumber(SENDER_ACC_NUMBER);
        accTransfer.setTransferAmount(TRANSFER_AMOUNT);
        accTransfer.setTransferCurrency(TRANSFER_CURRENCY);
        accTransfer.setDestinationNumber(DESTINATION_ACC_NUMBER);
        accTransfer.setTransferFrom(TRANSFER_FROM_ACC);
    }

    @Test
    public void accTransferPositive() {
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertTrue(actual);
    }

    @Test
    public void accTransferNegativeInsufficientFunds() {
        accTransfer.setTransferAmount(WRONG_TRANSFER_AMOUNT);
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertFalse(actual);
    }

    @Test
    public void accTransferNegativeWrongDestination() {
        String senderNumber = accTransfer.getSenderAccNumber();
        accTransfer.setDestinationNumber(senderNumber);
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertFalse(actual);
    }

    @Test
    public void accTransferNegativeAccPattern() {
        accTransfer.setSenderAccNumber(INVALID_ACC_NUMBER);
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertFalse(actual);
    }

    @Test
    public void accTransferNegativeCurrencyPattern() {
        accTransfer.setTransferCurrency(INVALID_CURRENCY_PATTERN);
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertFalse(actual);
    }
}

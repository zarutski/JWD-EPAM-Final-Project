package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.TransferData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccOperationValidationTest {

    private TransferData accTransfer;

    @Before
    public void setup(){
        accTransfer = new TransferData();
        accTransfer.setSenderAccountId(1);
        accTransfer.setSenderAccAmount(1000);
        accTransfer.setSenderAccNumber("BY10UNBS3135301412345678");
        accTransfer.setTransferAmount(500);
        accTransfer.setTransferCurrency("BYN");
        accTransfer.setDestinationNumber("BY12UNBS3078301434561278");
        accTransfer.setTransferFrom("acc");
    }

    @Test
    public void accTransferPositive() {
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertTrue(actual);
    }

    @Test
    public void accTransferNegativeInsufficientFunds() {
        accTransfer.setTransferAmount(1001);
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
        accTransfer.setSenderAccNumber("by300200100");
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertFalse(actual);
    }

    @Test
    public void accTransferNegativeCurrencyPattern() {
        accTransfer.setTransferCurrency("byn belarus");
        boolean actual = OperationValidator.transferDataValidation(accTransfer);
        assertFalse(actual);
    }
}

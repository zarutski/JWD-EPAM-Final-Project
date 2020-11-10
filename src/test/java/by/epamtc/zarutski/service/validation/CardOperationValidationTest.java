package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.TransferData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardOperationValidationTest {

    private TransferData cardTransfer;

    @Before
    public void setup(){
        cardTransfer = new TransferData();
        cardTransfer.setSenderAccountId(1);
        cardTransfer.setSenderAccAmount(1000);
        cardTransfer.setSenderAccNumber("BY10UNBS3135301412345678");
        cardTransfer.setTransferAmount(500);
        cardTransfer.setTransferCurrency("BYN");
        cardTransfer.setDestinationNumber("5185553343850289");
        cardTransfer.setTransferFrom("card");
        cardTransfer.setSenderCardId(1);
        cardTransfer.setSenderCardNumber("4539804056541904");
        cardTransfer.setConfirmationCode("332");
        cardTransfer.setCardState("активна");
    }

    @Test
    public void cardTransferPositive() {
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertTrue(actual);
    }

    @Test
    public void cardTransferNegativeInsufficientFunds() {
        cardTransfer.setTransferAmount(1001);
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
        cardTransfer.setCardState("заблокирована");
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeAccPattern() {
        cardTransfer.setSenderAccNumber("by300200100");
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeCardPattern() {
        cardTransfer.setSenderCardNumber("0000 0000");
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeCurrencyPattern() {
        cardTransfer.setTransferCurrency("byn belarus");
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

    @Test
    public void cardTransferNegativeCvvPattern() {
        cardTransfer.setConfirmationCode("0000");
        boolean actual = OperationValidator.transferDataValidation(cardTransfer);
        assertFalse(actual);
    }

}
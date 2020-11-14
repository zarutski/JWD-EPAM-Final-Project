package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class {@code TransferData} represents entity for storing data obout bank transfer operation
 *
 * @author Maksim Zarutski
 */
public class TransferData implements Serializable {

    private static final long serialVersionUID = -8749308504470687411L;

    private int senderAccountId;
    private long senderAccAmount;
    private String senderAccNumber;
    private long transferAmount;
    private String transferCurrency;
    private String destinationNumber;
    private String transferFrom;

    private int senderCardId;
    private String senderCardNumber;
    private String confirmationCode;
    private String cardState;

    public TransferData() {
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public long getSenderAccAmount() {
        return senderAccAmount;
    }

    public void setSenderAccAmount(long senderAccAmount) {
        this.senderAccAmount = senderAccAmount;
    }

    public String getSenderAccNumber() {
        return senderAccNumber;
    }

    public void setSenderAccNumber(String senderAccNumber) {
        this.senderAccNumber = senderAccNumber;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(long transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferCurrency() {
        return transferCurrency;
    }

    public void setTransferCurrency(String transferCurrency) {
        this.transferCurrency = transferCurrency;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public int getSenderCardId() {
        return senderCardId;
    }

    public void setSenderCardId(int senderCardId) {
        this.senderCardId = senderCardId;
    }

    public String getSenderCardNumber() {
        return senderCardNumber;
    }

    public void setSenderCardNumber(String senderCardNumber) {
        this.senderCardNumber = senderCardNumber;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getCardState() {
        return cardState;
    }

    public void setCardState(String cardState) {
        this.cardState = cardState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferData)) return false;
        TransferData data = (TransferData) o;
        return senderAccountId == data.senderAccountId &&
                senderAccAmount == data.senderAccAmount &&
                transferAmount == data.transferAmount &&
                senderCardId == data.senderCardId &&
                Objects.equals(senderAccNumber, data.senderAccNumber) &&
                Objects.equals(transferCurrency, data.transferCurrency) &&
                Objects.equals(destinationNumber, data.destinationNumber) &&
                Objects.equals(transferFrom, data.transferFrom) &&
                Objects.equals(senderCardNumber, data.senderCardNumber) &&
                Objects.equals(confirmationCode, data.confirmationCode) &&
                Objects.equals(cardState, data.cardState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderAccountId, senderAccAmount, senderAccNumber, transferAmount, transferCurrency, destinationNumber, transferFrom, senderCardId, senderCardNumber, confirmationCode, cardState);
    }

    @Override
    public String toString() {
        return "TransferData{" +
                "senderAccountId=" + senderAccountId +
                ", senderAccAmount=" + senderAccAmount +
                ", senderAccNumber='" + senderAccNumber + '\'' +
                ", transferAmount=" + transferAmount +
                ", transferCurrency='" + transferCurrency + '\'' +
                ", destinationNumber='" + destinationNumber + '\'' +
                ", transferFrom='" + transferFrom + '\'' +
                ", senderCardId=" + senderCardId +
                ", senderCardNumber='" + senderCardNumber + '\'' +
                ", confirmationCode='" + confirmationCode + '\'' +
                ", cardState='" + cardState + '\'' +
                '}';
    }
}
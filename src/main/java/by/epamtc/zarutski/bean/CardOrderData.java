package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class CardOrderData implements Serializable {

    private static final long serialVersionUID = -5741444076441329050L;

    private String cardNumber;
    private Date expirationDate;
    private String owner;
    private String cvvCode;
    private String paymentSystem;

    public CardOrderData() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardOrderData that = (CardOrderData) o;
        return Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(cvvCode, that.cvvCode) &&
                Objects.equals(paymentSystem, that.paymentSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, expirationDate, owner, cvvCode, paymentSystem);
    }

    @Override
    public String toString() {
        return "CardOrderData{" +
                "cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", owner='" + owner + '\'' +
                ", cvvCode='" + cvvCode + '\'' +
                ", paymentSystem='" + paymentSystem + '\'' +
                '}';
    }
}
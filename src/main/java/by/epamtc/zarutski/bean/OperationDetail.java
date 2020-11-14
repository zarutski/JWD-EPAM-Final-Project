package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class {@code OperationDetail} represents entity for details about the performed operation
 *
 * @author Maksim Zarutski
 */
public class OperationDetail implements Serializable {

    private static final long serialVersionUID = 3947781484493285194L;

    private int detailId;
    private long amount;
    private String currency;
    private String destinationAccount;

    public OperationDetail() {
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationDetail that = (OperationDetail) o;
        return detailId == that.detailId &&
                amount == that.amount &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(destinationAccount, that.destinationAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detailId, amount, currency, destinationAccount);
    }

    @Override
    public String toString() {
        return "OperationDetail{" +
                "detailId=" + detailId +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", destinationAccount='" + destinationAccount + '\'' +
                '}';
    }
}
package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The class {@code Account} represents entity for user's account
 *
 * @author Maksim Zarutski
 */
public class Account implements Serializable {

    private static final long serialVersionUID = -5441011950755538654L;

    private int accountId;
    private String accNumber;
    private long amount;
    private LocalDate openingDate;
    private String currency;
    private String state;

    public Account() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId &&
                amount == account.amount &&
                Objects.equals(accNumber, account.accNumber) &&
                Objects.equals(openingDate, account.openingDate) &&
                Objects.equals(currency, account.currency) &&
                Objects.equals(state, account.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accNumber, amount, openingDate, currency, state);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accNumber='" + accNumber + '\'' +
                ", amount=" + amount +
                ", openingDate=" + openingDate +
                ", currency='" + currency + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
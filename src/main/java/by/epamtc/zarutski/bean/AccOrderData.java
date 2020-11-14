package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * The class {@code AccOrderData} represents entity for account order's data
 *
 * @author Maksim Zarutski
 */
public class AccOrderData implements Serializable {

    private static final long serialVersionUID = 4719634179961126543L;

    private int userId;
    private String accNumber;
    private Date openingDate;
    private int currencyCode;

    public AccOrderData() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccOrderData that = (AccOrderData) o;
        return userId == that.userId &&
                currencyCode == that.currencyCode &&
                Objects.equals(accNumber, that.accNumber) &&
                Objects.equals(openingDate, that.openingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, accNumber, openingDate, currencyCode);
    }

    @Override
    public String toString() {
        return "AccOrderData{" +
                "userId=" + userId +
                ", accNumber='" + accNumber + '\'' +
                ", openingDate=" + openingDate +
                ", currencyCode=" + currencyCode +
                '}';
    }
}
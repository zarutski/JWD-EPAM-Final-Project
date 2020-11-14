package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The class {@code Card} represents entity for user's card
 *
 * @author Maksim Zarutski
 */
public class Card implements Serializable {

    private static final long serialVersionUID = -3474783307688862799L;

    private int cardId;
    private String cardNumber;
    private LocalDate expirationDate;
    private String owner;
    private String state;

    public Card() {
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
        Card card = (Card) o;
        return cardId == card.cardId &&
                Objects.equals(cardNumber, card.cardNumber) &&
                Objects.equals(expirationDate, card.expirationDate) &&
                Objects.equals(owner, card.owner) &&
                Objects.equals(state, card.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, cardNumber, expirationDate, owner, state);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", owner='" + owner + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
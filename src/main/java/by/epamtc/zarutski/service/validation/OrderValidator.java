package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.AccOrderData;
import by.epamtc.zarutski.bean.CardOrderData;

/**
 * The class {@code OrderValidator} validates data to perform a card order
 * <p>
 * Checks if certain fields of the {@code CardOrderData} and
 * {@code AccOrderData} objects are matching certain patterns
 *
 * @author Maksim Zarutski
 * @see CardOrderData
 * @see AccOrderData
 */
public class OrderValidator {

    private static final String ACC_PATTERN = "^([a-zA-z]|[0-9]){24}$";
    private static final String CARD_PATTERN = "^[0-9]{16}$";
    private static final String CONFIRMATION_CODE_PATTERN = "^[0-9]{3}$";

    private static final String OWNER_PATTERN = "^([A-Za-z]-*){2,}\\s([A-Za-z]-*){2,}$";
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}$";

    public static boolean cardValidation(CardOrderData orderData) {
        String cardNumber = orderData.getCardNumber();
        String expirationDate = orderData.getExpirationDate().toString();
        String owner = orderData.getOwner();
        String cvvCode = orderData.getCvvCode();

        return cardNumber.matches(CARD_PATTERN) && expirationDate.matches(DATE_PATTERN) &&
                owner.matches(OWNER_PATTERN) && cvvCode.matches(CONFIRMATION_CODE_PATTERN);
    }

    public static boolean accValidation(AccOrderData orderData) {
        String accNumber = orderData.getAccNumber();
        String openingDate = orderData.getOpeningDate().toString();

        return accNumber.matches(ACC_PATTERN) && openingDate.matches(DATE_PATTERN);
    }
}

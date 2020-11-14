package by.epamtc.zarutski.service.impl.generation;

import java.util.Random;

/**
 * The class {@code FacilityDataGenerator} provides generation
 * of card number, account and cvv-code for a new card order
 *
 * @author Maksim Zarutski
 */
public class FacilityDataGenerator {

    private static final Random random = new Random();

    private static final int PERSONAL_NUM_LENGTH = 12;
    private static final int CVV_LENGTH = 3;

    private static final String COUNTRY_CODE = "BY";
    private static final String BANK_SWIFT_NUMBER = "UNBS";
    private static final String DEFAULT_BALANCE_ACC = "3014";
    private static final String BANK_BIN_CODE = "46101";

    private static final int[] CONTROL_CODE_RATES_ONE = {4, 8, 7, 4, 1, 2, 9, 5, 3, 8, 1, 6};
    private static final int[] CONTROL_CODE_RATES_TWO = {3, 6, 5, 7, 4, 9, 8, 5, 5, 4, 5, 2};
    private static final int DIGIT_BOUND = 9;

    private static final String MASTERCARD = "MASTERCARD";
    private static final String VISA = "VISA";
    private static final int MASTERCARD_CODE = 5;
    private static final int VISA_CODE = 4;

    /**
     * Generates 3-digits cvv code
     *
     * @return {@code String} value of the generated code
     */
    public static String generateCvvCode() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < CVV_LENGTH; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }

        return builder.toString();
    }

    /**
     * Generates number of the user's account
     * <p>
     * Generated acc number has next format: AABB CCCC DDDD EEEE FFFF FFFF FFFF (no whitespaces)
     * Where:
     * AA - country code {@value COUNTRY_CODE}
     * BB - 2 control digits
     * CCCC - first 4 digits of bank's SWIFT-code {@value BANK_SWIFT_NUMBER}
     * DDDD - balance account (FROM 3019 to 3199) {@value DEFAULT_BALANCE_ACC}
     * FFFF FFFF FFFF - personal acc number
     *
     * @return {@code String} number of the generated account
     */
    public static String generateAccNum() {
        StringBuilder builder = new StringBuilder();

        String personalNum = generatePersonalAccNumber();
        String controlCode = generateControlCode(personalNum);

        builder.append(COUNTRY_CODE);
        builder.append(controlCode);
        builder.append(BANK_SWIFT_NUMBER);
        builder.append(DEFAULT_BALANCE_ACC);
        builder.append(personalNum);

        return builder.toString();
    }

    /**
     * Generates 12-digits personal account number
     *
     * @return generated {@code String} account number
     */
    private static String generatePersonalAccNumber() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < PERSONAL_NUM_LENGTH; i++) {
            int digit = random.nextInt(DIGIT_BOUND) + 1;
            builder.append(digit);
        }

        return builder.toString();
    }

    /**
     * Method generates 2-digits control code based on the account number
     * and using separate code rates for each generated control digit
     *
     * @param personalNum is a generated 12-digit account number
     * @return 2-digits control code
     */
    private static String generateControlCode(String personalNum) {
        StringBuilder builder = new StringBuilder();

        int firstDigit = getControlDigit(personalNum, CONTROL_CODE_RATES_ONE);
        int secondDigit = getControlDigit(personalNum, CONTROL_CODE_RATES_TWO);

        builder.append(firstDigit);
        builder.append(secondDigit);

        return builder.toString();
    }


    /**
     * Calculates control digit based on personal account number by multiplying defined coefficients
     *
     * @param personalNum is a generated 12-digit account number
     * @param controlRate is an array containing certain multiplier for each of the personal number digits
     * @return control digit value
     */
    private static int getControlDigit(String personalNum, int[] controlRate) {
        int[] product = new int[PERSONAL_NUM_LENGTH];

        for (int i = 0; i < PERSONAL_NUM_LENGTH; i++) {
            int digit = Character.getNumericValue(personalNum.charAt(i));
            product[i] = digit * controlRate[i];
        }

        int productSum = 0;
        for (int p : product) {
            productSum += p;
        }

        return DIGIT_BOUND - (productSum % DIGIT_BOUND);
    }

    /**
     * Generates number of the user's card
     * <p>
     * Generated card number has next format: KLLL LLMM MMMM MMNN (no whitespaces)
     * Where:
     * K - payment system international code
     * LLL LL - first 5 digits of bank's BIN-code {@value BANK_BIN_CODE}
     * MM MMMM MM - last 8 digits of user's account number
     * NN - 2 control digits of an account number
     *
     * @param accNumber     is a generated 12-digit account number
     * @param paymentSystem is a card's payment system user was choose
     * @return {@code String} generated card number
     */
    public static String generateCardNum(String accNumber, String paymentSystem) {
        StringBuilder builder = new StringBuilder();

        int paymentSystemCode = getPaymentSystemCode(paymentSystem);
        String cardUserNumber = accNumber.substring(16);
        String controlCode = accNumber.substring(2, 4);

        builder.append(paymentSystemCode);
        builder.append(BANK_BIN_CODE);
        builder.append(cardUserNumber);
        builder.append(controlCode);

        return builder.toString();
    }

    /**
     * Determines the payment system code based on the payment system value
     *
     * @param paymentSystem {@code String} value of the target payment system
     * @return payment system code value
     */
    private static int getPaymentSystemCode(String paymentSystem) {
        if (VISA.equals(paymentSystem)) {
            return VISA_CODE;
        }
        if (MASTERCARD.equals(paymentSystem)) {
            return MASTERCARD_CODE;
        }
        return 0;
    }
}

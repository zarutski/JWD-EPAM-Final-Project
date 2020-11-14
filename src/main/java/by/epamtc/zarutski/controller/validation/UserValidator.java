package by.epamtc.zarutski.controller.validation;

import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UpdateUserData;

import java.time.LocalDate;

/**
 * The class {@code UserValidator} validates user's data
 *
 * @author Maksim Zarutski
 */
public class UserValidator {

    private static final String DATE_OF_BIRTH_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    /**
     * Checks if {@code RegistrationData} object's data is correct
     *
     * @param data the {@code RegistrationData} contains data to user's registration
     * @return boolean value indicates that registration data is correct
     */
    public static boolean isRegistrationDataExists(RegistrationData data) {
        if (data == null) {
            return false;
        }

        String email = data.getEmail();
        String login = data.getLogin();
        String password = data.getPassword();
        String name = data.getName();
        String surname = data.getSurname();
        String patronymic = data.getPatronymic();

        String phoneNumber = data.getPhoneNumber();
        String passportSeries = data.getPassportSeries();
        String passportNumber = data.getPassportNumber();
        String address = data.getAddress();
        String postcode = data.getPostCode();

        LocalDate dateOfBirth = data.getDateOfBirth();
        if (dateOfBirth == null) {
            return false;
        }

        return isNullOrEmpty(email, login, password, name, surname, patronymic,
                phoneNumber, passportSeries, passportNumber, address, postcode);
    }

    /**
     * Checks if {@code UpdateUserData} object's data is correct
     *
     * @param data the {@code UpdateUserData} contains data to update user's data
     * @return boolean value indicates that new user's data is correct
     */
    public static boolean isUserDataExists(UpdateUserData data) {
        if (data == null) {
            return false;
        }

        String name = data.getName();
        String surname = data.getSurname();
        String patronymic = data.getPatronymic();

        String phoneNumber = data.getPhoneNumber();
        String address = data.getAddress();
        String postcode = data.getPostCode();

        return isNullOrEmpty(name, surname, patronymic, phoneNumber, address, postcode);
    }

    /**
     * Checks if {@code String} value of date is matches valid format to perform parsing
     *
     * @param date {@code String} value contains date to be parsed
     * @return boolean value indicates that date's format is valid
     */
    public static boolean isValidDateFormat(String date) {
        return (date != null) && date.matches(DATE_OF_BIRTH_PATTERN);
    }

    private static boolean isNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
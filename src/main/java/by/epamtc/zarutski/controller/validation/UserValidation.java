package by.epamtc.zarutski.controller.validation;

import by.epamtc.zarutski.bean.RegistrationData;

import java.time.LocalDate;

public class UserValidation {

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

    private static boolean isNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}

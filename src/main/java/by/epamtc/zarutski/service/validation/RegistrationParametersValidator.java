package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.RegistrationData;

public class RegistrationParametersValidator {

    private static final String EMAIL_PATTERN;
    private static final String PASSWORD_PATTERN;
    private static final String LOGIN_PATTERN;
    private static final String PHONE_NUMBER_PATTERN;
    private static final String PASSPORT_PATTERN;

    static {
        RegexResourceManager regexResourceManager = RegexResourceManager.getInstance();
        EMAIL_PATTERN = regexResourceManager.getValue(RegexParameter.REGEX_EMAIL);
        PASSWORD_PATTERN = regexResourceManager.getValue(RegexParameter.REGEX_PASSWORD);
        LOGIN_PATTERN = regexResourceManager.getValue(RegexParameter.REGEX_LOGIN);
        PHONE_NUMBER_PATTERN = regexResourceManager.getValue(RegexParameter.REGEX_PHONE_NUMBER);
        PASSPORT_PATTERN = regexResourceManager.getValue(RegexParameter.REGEX_PASSPORT);
    }

    public static boolean registrationDataValidation(RegistrationData data) {
        String email = data.getEmail();
        String password = data.getPassword();
        String login = data.getLogin();
        String phoneNumber = data.getPhoneNumber();
        String passportSeries = data.getPassportSeries();
        String passportNumber = data.getPassportNumber();
        String passport = passportSeries + passportNumber;

        return email.matches(EMAIL_PATTERN) && password.matches(PASSWORD_PATTERN) && login.matches(LOGIN_PATTERN)
                && phoneNumber.matches(PHONE_NUMBER_PATTERN) && passport.matches(PASSPORT_PATTERN);
    }
}
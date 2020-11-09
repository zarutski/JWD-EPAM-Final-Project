package by.epamtc.zarutski.service.validation;

import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UpdateUserData;
import org.apache.commons.fileupload.FileItem;

public class ParametersValidator {

    private static final String EMAIL_PATTERN;
    private static final String PASSWORD_PATTERN;
    private static final String LOGIN_PATTERN;
    private static final String PHONE_NUMBER_PATTERN;
    private static final String PASSPORT_PATTERN;
    public static final String EXTENSION_START = ".";
    public static final String EXTENSION_JPG = "jpg";
    public static final String EXTENSION_PNG = "png";

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

    public static boolean userDataValidation(UpdateUserData data) {
        String phoneNumber = data.getPhoneNumber();
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

    public static boolean extensionValidation(FileItem item) {
        String format = null;
        String fileName = item.getName();
        int index = fileName.lastIndexOf(EXTENSION_START);
        if (index > 0) {
            format = fileName.substring(index + 1);
            format = format.toLowerCase();

        }
        return EXTENSION_JPG.equals(format) || EXTENSION_PNG.equals(format);
    }
}
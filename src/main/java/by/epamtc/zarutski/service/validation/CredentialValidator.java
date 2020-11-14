package by.epamtc.zarutski.service.validation;

/**
 * The class {@code CredentialValidator} validates user's credentials
 *
 * @author Maksim Zarutski
 */
public class CredentialValidator {

    private static final int LOGIN_MIN = 4;
    private static final int LOGIN_MAX = 16;

    private static final int PASSWORD_MIN = 8;
    private static final int PASSWORD_MAX = 16;

    /**
     * Checks if credentials provided by user are valid
     *
     * @param login    provided by user
     * @param password provided by user
     * @return boolean value indicating if user's credentials are correct
     */
    public static boolean isCredentialCorrect(String login, String password) {
        return isLoginCorrect(login) && isPasswordCorrect(password);
    }

    private static boolean isLoginCorrect(String login) {
        return (login != null) && (login.length() >= LOGIN_MIN) && (login.length() <= LOGIN_MAX);
    }

    private static boolean isPasswordCorrect(String password) {
        return (password != null) && (password.length() >= PASSWORD_MIN) && (password.length() <= PASSWORD_MAX);
    }
}
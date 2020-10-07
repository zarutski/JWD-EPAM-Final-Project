package by.epamtc.zarutski.service.validation;

public class CredentialValidator {

    public static boolean isCredentialCorrect(String login, String password) {
        return isLoginCorrect(login) && isPasswordCorrect(password);
    }

    private static boolean isLoginCorrect(String login) {
        return login != null && login.length() >= 4;
    }

    private static boolean isPasswordCorrect(String password) {
        return password != null && password.length() >= 8;
    }
}

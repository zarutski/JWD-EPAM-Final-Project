package by.epamtc.zarutski.service.validation;


// простые вещи можно сделать статическими методами
// посложнее: интерфейсы и реализации
public class CredentialValidator {


    // TODO -- переписать примеры
    public static boolean isCorrect(String login, String password) {
        return isLoginCorrect(login) && isPasswordCorrect(password);
    }

    private static boolean isLoginCorrect(String login) {
        return login.length() >= 4;
    }

    private static boolean isPasswordCorrect(String password) {
        return password.length() >= 4;
    }
}

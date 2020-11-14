package by.epamtc.zarutski.service.validation;

import java.util.ResourceBundle;

/**
 * The {@code RegexResourceManager} class provides values from the .properties file using the supplied key
 *
 * @author Maksim Zarutski
 */
public class RegexResourceManager {

    private static final RegexResourceManager instance = new RegexResourceManager();

    private static final ResourceBundle bundle = ResourceBundle.getBundle("regex_parameters");

    public static RegexResourceManager getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return bundle.getString(key);
    }
}
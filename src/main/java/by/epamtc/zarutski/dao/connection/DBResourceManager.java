package by.epamtc.zarutski.dao.connection;

import java.util.ResourceBundle;

/**
 * The {@code DBResourceManager} class provides values from the .properties file using the supplied key
 *
 * @author Maksim Zarutski
 */
public class DBResourceManager {

    private static final DBResourceManager instance = new DBResourceManager();

    private static final ResourceBundle bundle = ResourceBundle.getBundle("db_config");

    public static DBResourceManager getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return bundle.getString(key);
    }
}
package by.epamtc.zarutski.dao.connection;

import java.util.ResourceBundle;

public class DBResourceManager {

	private static final DBResourceManager instance = new DBResourceManager();

	private ResourceBundle bundle = ResourceBundle.getBundle("db_config");
	
	public static DBResourceManager getInstance() {
		return instance;
	}
	
	public String getValue(String key) {
		return bundle.getString(key);
	}
}

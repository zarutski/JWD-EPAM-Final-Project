package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.dao.UserDAO;
import by.epamtc.zarutski.dao.connection.ConnectionPool;
import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UserData;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class SQLUserDAOImpl implements UserDAO {
	
	private final ConnectionPool connectionPool = ConnectionPool.getInstance();
		
	private static final String DB_USER_ID = "id"; // идентифицировать id среди таблиц
	private static final String DB_USER_ROLE_NAME = "role_name";
	
	
	private static final String DB_USER_EMAIL = "email";
	private static final String DB_USER_NAME = "name";
	private static final String DB_USER_SURNAME = "surname";
	private static final String DB_USER_PATRONYMIC = "patronymic";
	private static final String DB_USER_PHONE_NUMBER = "phone_number";
	private static final String DB_USER_PASSPORT_SERIES = "passport_series";
	private static final String DB_USER_PASSPORT_NUMBER = "passport_number";
	private static final String DB_USER_BIRTH_DATE = "date_birth";
	private static final String DB_USER_ADDRESS = "address";
	private static final String DB_USER_POST_CODE = "post_code";
	private static final String DB_USER_PHOTO_LINK = "user_photo_link";


	// TODO каждое поле
	private static final String SELECT_USER_DATA_BY_USER = "SELECT * FROM users INNER JOIN user_details " +
            "ON users.user_details_id = user_details.id WHERE users.id=?";
	
	private static final String SELECT_USER_AUTHENTICATION_DATA = "SELECT * FROM users INNER JOIN user_roles " +
            "ON users.role_code = user_roles.role_code WHERE users.login=? AND users.password=?";
	
	// INSERTS
	private static final String INSERT_INTO_USER_DETAILS = "INSERT INTO `payment_app`.`user_details` (`name`, `surname`, `patronymic`, `phone_number`,"
			+ " `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_INTO_USERS = "INSERT INTO `payment_app`.`users` (`login`, `password`, `email`, `role_code`, `user_details_id`)" +
            " VALUES (?, ?, ?, ?, ?)";       ;

	
     @Override
    public AuthenticationData authentication(String login, String password) throws DAOException{
    	 
    	 Connection con = null;
         PreparedStatement ps = null;
         ResultSet resultSet = null;
         AuthenticationData userAuthentication = null;
         
         try {
             con = connectionPool.takeConnection();
             ps = con.prepareStatement(SELECT_USER_AUTHENTICATION_DATA);

             ps.setString(1, login);
             ps.setString(2, password);

             resultSet = ps.executeQuery();
             if (resultSet.next()) {
            	 
            	 int id = Integer.parseInt(resultSet.getString(DB_USER_ID));
            	 String roleName = resultSet.getString(DB_USER_ROLE_NAME);
            	 
            	 userAuthentication = new AuthenticationData();
            	 userAuthentication.setUserId(id);
            	 userAuthentication.setUserRole(roleName);
             }

         
         } catch(ConnectionPoolException e) {
         	throw new DAOException("ConnectionPool exception", e);
         } catch (SQLException e) {
  			throw new DAOException("Authentication  exception", e);
         } finally {
 			try {
 				connectionPool.closeConnection(con, ps, resultSet);
 			} catch (ConnectionPoolException e) {
 				throw new DAOException("Error close connection", e);
 			}
 		}
    	 
        return userAuthentication;
    }

     
     @Override
    public boolean registration(RegistrationData registrationData) throws DAOException{
    	 
    	 boolean registrationSuccessful = false;
    	 
    	 Connection con = null;
         PreparedStatement insertUserDetailsStatement = null;
         PreparedStatement insertUsersStatement = null;
         
         try {
        	 con = connectionPool.takeConnection(); 
             
             insertUserDetailsStatement = con.prepareStatement(INSERT_INTO_USER_DETAILS, Statement.RETURN_GENERATED_KEYS); // инсерт в одну
             insertUsersStatement = con.prepareStatement(INSERT_INTO_USERS); // инсерт в другую
             
             
             String login = registrationData.getLogin();
     		 String password = registrationData.getPassword();
     		 String email = registrationData.getEmail();
     		 
             String name = registrationData.getName();
     		 String surname = registrationData.getSurname();
     		 String patronymic = registrationData.getPatronymic();
     		 String phoneNumber = registrationData.getPhoneNumber();
     		 
     		 String passportSeries = registrationData.getPassportSeries();
    		 String passportNumber = registrationData.getPassportNumber();
    		 LocalDate localDateOfBirth = registrationData.getDateOfBirth();
    		 Date dateOfBirth = Date.valueOf(localDateOfBirth);
    		 String address = registrationData.getAddress();
    		 String postCode = registrationData.getPostCode();


    		 // начать транзакцию (равильно завершить)
    		 // проверить существует ли юзер с таким логином  
     		 
    		 insertUserDetailsStatement.setString(1, name);
    		 insertUserDetailsStatement.setString(2, surname);
    		 insertUserDetailsStatement.setString(3, patronymic);
    		 insertUserDetailsStatement.setString(4, phoneNumber);
    		 insertUserDetailsStatement.setString(5, passportSeries);
    		 insertUserDetailsStatement.setString(6, passportNumber);
    		 insertUserDetailsStatement.setDate(7, dateOfBirth);
    		 insertUserDetailsStatement.setString(8, address);
    		 insertUserDetailsStatement.setString(9, postCode);

    		 
    		 
    		 int affectedRows = insertUserDetailsStatement.executeUpdate();

    		 if (affectedRows == 0) {
    			 throw new SQLException("Insertion failed, no rows affected.");
    		 }
    		 
    		 
    		 // получить сгенерированный ключ
    		 // и произвести ещё одну вставку
    		 int userDetailsId = 0;
    		 
    		 ResultSet generatedKeys = null;
    		 try {
    			 	generatedKeys = insertUserDetailsStatement.getGeneratedKeys();
    	            if (generatedKeys.next()) {
    	            	userDetailsId  = generatedKeys.getInt(1);
    	            }
    	            else {
    	                throw new SQLException("Failed, no ID obtained");
    	            }
    		 } finally{
    			 try {
    	  				connectionPool.closeStatement(insertUserDetailsStatement, generatedKeys);
    	  			} catch (ConnectionPoolException e) {
    	  				throw new DAOException("Error close statement", e);
    	  			}
    		 }
    		 
    		 
    		 // изменить (добавить алгоритм)
    		 int defaultUserRole = 1;
    		 
    		 
    		insertUsersStatement.setString(1, login);
    		insertUsersStatement.setString(2, password);
    		insertUsersStatement.setString(3, email);
    		insertUsersStatement.setInt(4, defaultUserRole);
    		insertUsersStatement.setInt(5, userDetailsId);
    		 

  			if (insertUsersStatement.executeUpdate() == 1) {
 				return true;
 			}

  			// нормальные сообщеия, логи - MAGIC VALUE
         } catch(ConnectionPoolException e) {
         	throw new DAOException("ConnectionPool exception", e);
         } catch (SQLException e) {
  			throw new DAOException("SQL exception", e);
         } finally {
  			try {
  				connectionPool.closeConnection(con, insertUsersStatement);
  			} catch (ConnectionPoolException e) {
  				throw new DAOException("Error close connection", e);
  			}
  		}
    	 
         return registrationSuccessful;
    }

     
	@Override
	public UserData getUserData(int userId, String roleName) throws DAOException {
		
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        UserData userData = null;
        
        try {
        	con = connectionPool.takeConnection();     
            ps = con.prepareStatement(SELECT_USER_DATA_BY_USER);
            ps.setInt(1, userId);
            
            resultSet = ps.executeQuery();
            
            if (resultSet.next()) {
           	 
	           	String email= resultSet.getString(DB_USER_EMAIL);
	           	String name = resultSet.getString(DB_USER_NAME);
	           	String surname = resultSet.getString(DB_USER_SURNAME);
	           	String patronymic = resultSet.getString(DB_USER_PATRONYMIC);
	           	String phoneNumber = resultSet.getString(DB_USER_PHONE_NUMBER);
	           	String passportSeries = resultSet.getString(DB_USER_PASSPORT_SERIES);
	           	String passportNumber = resultSet.getString(DB_USER_PASSPORT_NUMBER);
	           	LocalDate dateOfBirth = resultSet.getDate(DB_USER_BIRTH_DATE).toLocalDate(); // добавить DateTimeFormatter
	           	String address = resultSet.getString(DB_USER_ADDRESS);
	           	String postCode= resultSet.getString(DB_USER_POST_CODE);
	           	String photoLink = resultSet.getString(DB_USER_PHOTO_LINK);
	           	
	           	
	           	userData = new UserData();	           	
	           	userData.setEmail(email);
	           	userData.setName(name);
	           	userData.setSurname(surname);
	           	userData.setPatronymic(patronymic);
	           	userData.setPhoneNumber(phoneNumber);
	           	userData.setPassportSeries(passportSeries);
	           	userData.setPassportNumber(passportNumber);
	           	userData.setDateOfBirth(dateOfBirth);
	           	userData.setAddress(address);
	           	userData.setPostCode(postCode);
	           	userData.setPhotoLink(photoLink);
	           	
	           	
	           	userData.setUserId(userId);
	           	userData.setRoleName(roleName);
            }

        } catch(ConnectionPoolException e) {
        	throw new DAOException("ConnectionPool exception", e);
        } catch (SQLException e) {
 			throw new DAOException("SQL exception", e);
        } finally {
 			try {
 				connectionPool.closeConnection(con, ps, resultSet);
 			} catch (ConnectionPoolException e) {
 				throw new DAOException("Error close connection", e);
 			}
 		}
   	 
        return userData;
	}
}

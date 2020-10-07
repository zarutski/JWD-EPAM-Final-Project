package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.dao.UserDAO;
import by.epamtc.zarutski.dao.connection.ConnectionPool;
import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.UserExistsDAOException;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UserData;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SQLUserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger(SQLUserDAOImpl.class);
    
    private static final String LOG_USER_NOT_FOUND = "User not found";
    private static final String LOG_DB_AUTHENTICATION_ERROR = "DB error during authentication";
    private static final String LOG_DB_INSERTION = "DB insertion failed, no rows affected";
    private static final String LOG_KEYS_GENERATION = "No keys were generated";
    private static final String LOG_SUCCESSFUL_REGISTRATION = "Registration transaction finished successfully";

    private static final String LOG_USER_EXISTS = "User already exists";
    private static final String LOG_DB_REGISTRATION_ERROR = "DB error during registration";
    private static final String LOG_CONNECTION_POOL_ERROR = "Connection pool error";
    private static final String LOG_ERROR_CLOSING = "Error closing resources";

    private static final String LOG_ROLLBACK_ERROR = "Transaction rollback error";
    private static final String LOG_AUTO_COMMIT = "Auto-commit error";

    private static final String LOG_ERROR_FETCHING_DATA = "DB error during fetching user data";

    private static final String KEY_ERROR_MESSAGE = "Key generation failed";
    private static final String INSERTION_ERROR_MESSAGE = "DB insertion failed";

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String DB_USER_ID = "id";
    private static final String DB_USER_ROLE_NAME = "role_name";
    private static final int DEFAULT_USER_ROLE = 1;

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


    private static final String SELECT_USER_DATA_BY_USER = "SELECT * FROM users INNER JOIN user_details " +
            "ON users.user_details_id = user_details.id WHERE users.id=?";

    private static final String SELECT_USER_AUTHENTICATION_DATA = "SELECT * FROM users INNER JOIN user_roles " +
            "ON users.role_code = user_roles.role_code WHERE users.login=? AND users.password=?";


    private static final String INSERT_INTO_USER_DETAILS = "INSERT INTO `payment_app`.`user_details` (`name`, `surname`, `patronymic`, `phone_number`,"
            + " `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_INTO_USERS = "INSERT INTO `payment_app`.`users` (`login`, `password`, `email`, `role_code`, `user_details_id`)" +
            " VALUES (?, ?, ?, ?, ?)";
    ;


    @Override
    public AuthenticationData authentication(String login, String password) throws DAOException {

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

                int id = Integer.parseInt(resultSet.getString(DB_USER_ID)); // исключение
                String roleName = resultSet.getString(DB_USER_ROLE_NAME);

                userAuthentication = new AuthenticationData();
                userAuthentication.setUserId(id);
                userAuthentication.setUserRole(roleName);
            } else {
                logger.info(LOG_USER_NOT_FOUND);
            }

        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_DB_AUTHENTICATION_ERROR, e);
            throw new DAOException(e);
        } finally {
            try {
                if (connectionPool != null) {
                    connectionPool.closeConnection(con, ps, resultSet);
                }
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING, e);
                throw new DAOException(e);
            }
        }

        return userAuthentication;
    }


    @Override
    public boolean registration(RegistrationData registrationData) throws DAOException {

        boolean registrationSuccessful = false;

        Connection con = null;
        PreparedStatement insertUserDetailsStatement = null;
        PreparedStatement insertUsersStatement = null;

        try {
            con = connectionPool.takeConnection();
            con.setAutoCommit(false);

            insertUserDetailsStatement = con.prepareStatement(INSERT_INTO_USER_DETAILS, Statement.RETURN_GENERATED_KEYS); // инсерт в одну таблицу
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
                logger.error(LOG_DB_INSERTION);
                throw new SQLException(INSERTION_ERROR_MESSAGE);
            }


            // получить сгенерированный ключ, затем произвести ещё одну вставку
            ResultSet generatedKeys = null;
            int userDetailsId = 0;
            try {
                generatedKeys = insertUserDetailsStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userDetailsId = generatedKeys.getInt(1);
                } else {
                    logger.error(LOG_KEYS_GENERATION);
                    throw new SQLException(KEY_ERROR_MESSAGE);
                }
            } finally {
                try {
                    if (connectionPool != null) {
                        connectionPool.closeStatement(insertUserDetailsStatement, generatedKeys);
                    }
                } catch (ConnectionPoolException e) {
                    logger.error(LOG_ERROR_CLOSING, e);
                    throw new DAOException(e);
                }
            }


            int userRole = DEFAULT_USER_ROLE;

            insertUsersStatement.setString(1, login);
            insertUsersStatement.setString(2, password);
            insertUsersStatement.setString(3, email);
            insertUsersStatement.setInt(4, userRole);
            insertUsersStatement.setInt(5, userDetailsId);

            if (insertUsersStatement.executeUpdate() == 1) {
                con.commit();
                logger.info(LOG_SUCCESSFUL_REGISTRATION);
                registrationSuccessful = true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            logger.warn(LOG_USER_EXISTS, e);
            throw new UserExistsDAOException(e);
        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_DB_REGISTRATION_ERROR, e);
            throw new DAOException(e);
        } finally {
            
        	// TODO - взять из пула, написать по-человечески
            if (!registrationSuccessful) {
                rollback(con);
            }
            finishTransaction(con);
            try {
                if (connectionPool != null) {
                    connectionPool.closeConnection(con, insertUsersStatement);
                }
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING, e);
                throw new DAOException(e);
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

                String email = resultSet.getString(DB_USER_EMAIL);
                String name = resultSet.getString(DB_USER_NAME);
                String surname = resultSet.getString(DB_USER_SURNAME);
                String patronymic = resultSet.getString(DB_USER_PATRONYMIC);
                String phoneNumber = resultSet.getString(DB_USER_PHONE_NUMBER);
                String passportSeries = resultSet.getString(DB_USER_PASSPORT_SERIES);
                String passportNumber = resultSet.getString(DB_USER_PASSPORT_NUMBER);
                LocalDate dateOfBirth = resultSet.getDate(DB_USER_BIRTH_DATE).toLocalDate();
                String address = resultSet.getString(DB_USER_ADDRESS);
                String postCode = resultSet.getString(DB_USER_POST_CODE);
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

        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_ERROR_FETCHING_DATA, e);
            throw new DAOException(e);
        } finally {
            try {
                if (connectionPool != null) {
                    connectionPool.closeConnection(con, ps, resultSet);
                }
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING, e);
                throw new DAOException(e);
            }
        }

        return userData;
    }


    private void rollback(Connection connection) throws DAOException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error(LOG_ROLLBACK_ERROR);
            throw new DAOException(e);
        }
    }

    private void finishTransaction(Connection connection) throws DAOException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(LOG_AUTO_COMMIT);
            throw new DAOException(e);
        }
    }
}

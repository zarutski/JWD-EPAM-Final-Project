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
import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger(SQLUserDAOImpl.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String LOG_USER_NOT_FOUND = "User not found";
    private static final String LOG_DB_AUTHENTICATION_ERROR = "DB error during authentication";
    private static final String LOG_KEYS_GENERATION = "No keys were generated";
    private static final String LOG_SUCCESSFUL_REGISTRATION = "Registration transaction finished successfully";
    private static final String LOG_USER_EXISTS = "User already exists";

    private static final String LOG_DB_REGISTRATION_ERROR = "DB error during registration";
    private static final String LOG_CONNECTION_POOL_ERROR = "Connection pool error";
    private static final String LOG_ERROR_CLOSING_RESOURCES = "Error closing resources";
    private static final String LOG_ERROR_FETCHING_DATA = "DB error during fetching user data";

    private static final String KEY_ERROR_MESSAGE = "Key generation failed";
    private static final String INSERTION_ERROR_MESSAGE = "DB insertion failed";

    private static final String DB_USER_ID = "id";
    private static final String DB_USER_ROLE_NAME = "role_name";
    private static final String DB_USER_PASSWORD = "password";
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
    public static final int SALT_LENGTH = 29;
    private static final int DEFAULT_USER_ROLE = 1;

    private static final String SELECT_USER_DATA_BY_USER = "SELECT * FROM users INNER JOIN user_details " +
            "ON users.user_details_id = user_details.id WHERE users.id=?";

    private static final String SELECT_AUTHENTICATION_DATA_BY_LOGIN = "SELECT * FROM users INNER JOIN user_roles " +
            "ON users.role_code = user_roles.role_code WHERE users.login=?";

    private static final String INSERT_INTO_USER_DETAILS = "INSERT INTO `payment_app`.`user_details` (`name`, `surname`, `patronymic`, `phone_number`,"
            + " `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_INTO_USERS = "INSERT INTO `payment_app`.`users` (`login`, `password`, `email`, `role_code`, `user_details_id`)" +
            " VALUES (?, ?, ?, ?, ?)";


    @Override
    public AuthenticationData authentication(String login, String password) throws DAOException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        AuthenticationData userAuthentication = null;

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_AUTHENTICATION_DATA_BY_LOGIN);
            ps.setString(1, login);

            resultSet = ps.executeQuery();
            if (resultSet.next()) {

                String fetchedPassword = resultSet.getString(DB_USER_PASSWORD);
                String hashedPassword = hashPassword(password, fetchedPassword);


                if (hashedPassword.equals(fetchedPassword)) {

                    int id = resultSet.getInt(DB_USER_ID);
                    String roleName = resultSet.getString(DB_USER_ROLE_NAME);

                    userAuthentication = new AuthenticationData();
                    userAuthentication.setUserId(id);
                    userAuthentication.setUserRole(roleName);
                }
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
                logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
                throw new DAOException(e);
            }
        }

        return userAuthentication;
    }

    private String hashPassword(String password, String fetchedPassword) {
        String salt = fetchedPassword.substring(0, SALT_LENGTH);
        return BCrypt.hashpw(password, salt);
    }


    @Override
    public boolean registration(RegistrationData registrationData) throws DAOException {

        Connection con = null;
        PreparedStatement insertUserDetailsStatement = null;
        PreparedStatement insertUsersStatement = null;
        boolean registrationSuccessful = false;

        try {
            con = connectionPool.takeConnection();
            con.setAutoCommit(false);

            insertUserDetailsStatement = con.prepareStatement(INSERT_INTO_USER_DETAILS, Statement.RETURN_GENERATED_KEYS); // инсерт в одну таблицу

            insertUserDetailsStatement.setString(1, registrationData.getName());
            insertUserDetailsStatement.setString(2, registrationData.getSurname());
            insertUserDetailsStatement.setString(3, registrationData.getPatronymic());
            insertUserDetailsStatement.setString(4, registrationData.getPhoneNumber());
            insertUserDetailsStatement.setString(5, registrationData.getPassportSeries());
            insertUserDetailsStatement.setString(6, registrationData.getPassportNumber());

            LocalDate localDateOfBirth = registrationData.getDateOfBirth();
            Date dateOfBirth = Date.valueOf(localDateOfBirth);
            insertUserDetailsStatement.setDate(7, dateOfBirth);

            insertUserDetailsStatement.setString(8, registrationData.getAddress());
            insertUserDetailsStatement.setString(9, registrationData.getPostCode());

            int affectedRows = insertUserDetailsStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException(INSERTION_ERROR_MESSAGE);
            }


            // получить сгенерированный ключ, затем произвести ещё одну вставку
            int userDetailsId = extractGeneratedId(insertUserDetailsStatement);
            insertUsersStatement = con.prepareStatement(INSERT_INTO_USERS); // инсерт в другую

            String password = registrationData.getPassword();
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            insertUsersStatement.setString(1, registrationData.getLogin());
            insertUsersStatement.setString(2, hashedPassword);
            insertUsersStatement.setString(3, registrationData.getEmail());
            insertUsersStatement.setInt(4, DEFAULT_USER_ROLE);
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

            try {
                if (connectionPool != null) {
                    rollbackIfFailed(connectionPool, con, registrationSuccessful);
                    connectionPool.finishTransaction(con);
                    connectionPool.closeConnection(con, insertUsersStatement);
                }
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
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
                logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
                throw new DAOException(e);
            }
        }

        return userData;
    }

    private int extractGeneratedId(PreparedStatement ps) throws DAOException {
        ResultSet generatedKeys = null;
        int userDetailsId = 0;

        try {
            generatedKeys = ps.getGeneratedKeys();

            if (generatedKeys.next()) {
                userDetailsId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(LOG_KEYS_GENERATION);
            throw new DAOException(KEY_ERROR_MESSAGE);
        } finally {
            try {
                if (connectionPool != null) {
                    connectionPool.closeStatement(ps, generatedKeys);
                }
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
                throw new DAOException(e);
            }
        }
        return userDetailsId;
    }

    private void rollbackIfFailed(ConnectionPool pool, Connection con, boolean succeed) throws ConnectionPoolException {
        if (!succeed) {
            pool.rollback(con);
        }
    }
}
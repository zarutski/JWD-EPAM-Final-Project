package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.UpdateUserData;
import by.epamtc.zarutski.dao.UserDAO;
import by.epamtc.zarutski.dao.connection.ConnectionPool;
import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.UserExistsDAOException;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.bean.UserData;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import by.epamtc.zarutski.dao.exception.WrongDataDAOException;
import by.epamtc.zarutski.dao.search.SearchQueryProvider;
import org.apache.commons.fileupload.FileItem;
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
    private static final String LOG_WRONG_ROLE_CODE = "wrong role code";

    public static final String LOG_WRITING_FILE_ERROR = "writing file error";
    public static final String LOG_PREVIOUS_PHOTO = "prev photo was deleted";

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

    public static final String FILE_DIR = "http://localhost:8080/uploaded_files/tbank/";
    public static final String USER_PHOTO_PREFIX = "user_photo_profile_";
    public static final String DOWNLOAD_DIR = "C:\\Users\\Random\\eclipse-workspace\\.metadata\\.plugins\\" +
            "org.eclipse.wst.server.core\\tmp0\\webapps\\uploaded_files\\tbank\\";

    public static final String PERCENT_SYMBOL = "%";
    private static final int DEFAULT_USER_ROLE = 1;
    public static final int SALT_LENGTH = 29;


    private static final String SELECT_USER_DATA_BY_USER = "SELECT u.email, ud.surname, ud.name, ud.patronymic, " +
            "ud.phone_number, ud.passport_series, ud.passport_number, " +
            "ud.date_birth, ud.address, ud.post_code, ud.user_photo_link, ur.role_name " +
            "FROM users AS u " +
            "INNER JOIN user_details AS ud ON u.user_details_id = ud.id " +
            "INNER JOIN user_roles AS ur ON u.role_code = ur.role_code " +
            "WHERE u.id=?";

    private static final String SELECT_AUTHENTICATION_DATA_BY_LOGIN = "SELECT * FROM users INNER JOIN user_roles " +
            "ON users.role_code = user_roles.role_code WHERE users.login=?";

    private static final String INSERT_INTO_USER_DETAILS = "INSERT INTO `payment_app`.`user_details` " +
            "(`name`, `surname`, `patronymic`, `phone_number`, `passport_series`, " +
            "`passport_number`, `date_birth`, `address`, `post_code`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_INTO_USERS = "INSERT INTO `payment_app`.`users` " +
            "(`login`, `password`, `email`, `role_code`, `user_details_id`) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_USER_DETAILS = "UPDATE user_details SET " +
            "surname = ?, name = ?, patronymic = ?, phone_number = ?, address = ?, post_code = ? " +
            "WHERE id = ?";

    private static final String CHANGE_USER_ROLE = "UPDATE users SET role_code = ? WHERE id = ?";

    private static final String USER_PHOTO_UPLOAD = "UPDATE user_details SET user_photo_link = ? WHERE id = ?";

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
            closeResources(con, ps, resultSet);
        }

        return userAuthentication;
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

            // инсерт в одну таблицу
            insertUserDetailsStatement = con.prepareStatement(INSERT_INTO_USER_DETAILS, Statement.RETURN_GENERATED_KEYS);

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
    public UserData getUserData(int userId) throws DAOException {

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
                userData = getUser(userId, resultSet);
            }

        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_ERROR_FETCHING_DATA, e);
            throw new DAOException(e);
        } finally {
            closeResources(con, ps, resultSet);
        }

        return userData;
    }


    @Override
    public List<UserData> findUsers(String searchRequest) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        List<UserData> users = null;

        try {
            searchRequest = searchRequest.trim();
            String query = getSearchQuery(searchRequest);

            con = connectionPool.takeConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, PERCENT_SYMBOL + searchRequest + PERCENT_SYMBOL);
            resultSet = ps.executeQuery();

            users = new ArrayList<>();
            UserData user = null;

            while (resultSet.next()) {
                int userId = resultSet.getInt(DB_USER_ID);
                user = getUser(userId, resultSet);
                users.add(user);
            }

        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_ERROR_FETCHING_DATA, e);
            throw new DAOException(e);
        } finally {
            closeResources(con, ps, resultSet);
        }

        return users;
    }


    @Override
    public void updateUser(UpdateUserData userData) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(UPDATE_USER_DETAILS);

            ps.setString(1, userData.getSurname());
            ps.setString(2, userData.getName());
            ps.setString(3, userData.getPatronymic());
            ps.setString(4, userData.getPhoneNumber());
            ps.setString(5, userData.getAddress());
            ps.setString(6, userData.getPostCode());
            ps.setInt(7, userData.getUserId());

            ps.executeUpdate();

            // TODO --- SQLIntegrityConstraintViolationException надо бы, мало ли
        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_ERROR_FETCHING_DATA, e);
            throw new DAOException(e);
        } finally {
            closeResources(con, ps);
        }
    }

    @Override
    public void changeUserRole(int userId, int roleCode) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(CHANGE_USER_ROLE);

            ps.setInt(1, roleCode);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error(LOG_WRONG_ROLE_CODE, e);
            throw new WrongDataDAOException(e);
        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_ERROR_FETCHING_DATA, e);
            throw new DAOException(e);
        } finally {
            closeResources(con, ps);
        }
    }

    @Override
    public void uploadUserPhoto(FileItem item, int userId) throws DAOException {

        Connection con = null;
        PreparedStatement ps = null;

        String userPhotoName = USER_PHOTO_PREFIX + userId;
        String userPhotoDir = DOWNLOAD_DIR + userPhotoName;
        String userPhotoLink = FILE_DIR + userPhotoName;

        File file = new File(userPhotoDir);
        if (file.exists()) {
            file.delete();
            logger.info(LOG_PREVIOUS_PHOTO);
        }

        try {
            item.write(new File(userPhotoDir));

            con = connectionPool.takeConnection();
            ps = con.prepareStatement(USER_PHOTO_UPLOAD);

            ps.setString(1, userPhotoLink);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_ERROR_FETCHING_DATA, e);
            throw new DAOException(e);
        } catch (Exception e) {
            logger.error(LOG_WRITING_FILE_ERROR, e);
        } finally {
            closeResources(con, ps);
        }
    }

    private void closeResources(Connection con, PreparedStatement ps) throws DAOException {
        try {
            if (connectionPool != null) {
                connectionPool.closeConnection(con, ps);
            }
        } catch (ConnectionPoolException e) {
            logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
            throw new DAOException(e);
        }
    }

    private void closeResources(Connection con, PreparedStatement ps, ResultSet resultSet) throws DAOException {
        try {
            if (connectionPool != null) {
                connectionPool.closeConnection(con, ps, resultSet);
            }
        } catch (ConnectionPoolException e) {
            logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
            throw new DAOException(e);
        }
    }

    private void rollbackIfFailed(ConnectionPool pool, Connection con, boolean succeed) throws ConnectionPoolException {
        if (!succeed) {
            pool.rollback(con);
        }
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

    private String hashPassword(String password, String fetchedPassword) {
        String salt = fetchedPassword.substring(0, SALT_LENGTH);
        return BCrypt.hashpw(password, salt);
    }


    private String getSearchQuery(String searchRequest) {
        SearchQueryProvider provider = SearchQueryProvider.getInstance();
        return provider.getSearchQueryByDestination(searchRequest);
    }

    private UserData getUser(int userId, ResultSet resultSet) throws SQLException {
        UserData userData = null;

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
        String roleName = resultSet.getString(DB_USER_ROLE_NAME);

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
        userData.setRoleName(roleName);

        userData.setUserId(userId);

        return userData;
    }

    // в валидаторах и методах порядок проверки строк --- сначала константа, потом проверяемое значение (мало ли null)
    // валидация в сервисах на длину поисковой строки, по суммарной длине имени/фамилии
    // валидация в контроллере поисковой строки на null и пустоту
}
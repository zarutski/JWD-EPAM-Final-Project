package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.dao.AccountDAO;
import by.epamtc.zarutski.dao.connection.ConnectionPool;
import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;
import by.epamtc.zarutski.dao.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLAccountDAOImpl implements AccountDAO {

    private static final Logger logger = LogManager.getLogger(SQLCardDAOImpl.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String LOG_CONNECTION_POOL_ERROR = "Connection pool error";
    private static final String LOG_ERROR_CLOSING_RESOURCES = "Error closing resources";
    private static final String LOG_ERROR_FETCHING_DATA = "DB error during fetching user data";

    private static final String SELECT_USER_ACCOUNTS = "SELECT a.account_id, a.number, a.amount, a.opening_date, " +
            "c.currency, a_s.description FROM users " +
            "INNER JOIN accounts AS a ON users.id=a.users_id " +
            "INNER JOIN account_states AS a_s ON a.state_code=a_s.state_code " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id " +
            "WHERE users.id= ?";

    @Override
    public List<Account> getUserAccounts(int userId) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Account> userAccounts = new ArrayList<>();

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_USER_ACCOUNTS);
            ps.setInt(1, userId);

            resultSet = ps.executeQuery();

            Account account = null;
            while (resultSet.next()) {

                int accountId = resultSet.getInt("account_id");
                String accNumber = resultSet.getString("number");
                long amount = resultSet.getLong("amount"); // ВОПРОС
                LocalDate openingDate = resultSet.getDate("opening_date").toLocalDate();
                ;
                String currency = resultSet.getString("currency");
                String state = resultSet.getString("description");

                account = new Account();
                account.setAccountId(accountId);
                account.setAccNumber(accNumber);
                account.setOpeningDate(openingDate);
                account.setAmount(amount);
                account.setCurrency(currency);
                account.setState(state);
                userAccounts.add(account);
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

        return userAccounts;
    }

}
package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.*;
import by.epamtc.zarutski.dao.FacilityDAO;
import by.epamtc.zarutski.dao.connection.ConnectionPool;
import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code FacilityDAOImpl} provides implementation of the {@code FacilityDAO} interface
 *
 * @author Maksim Zarutski
 */
public class FacilityDAOImpl implements FacilityDAO {

    private static final Logger logger = LogManager.getLogger(FacilityDAOImpl.class);

    private static final String DEST_CARD = "card";
    private static final String DEST_ACC = "account";

    private static final String DB_CARD_ID = "credit_card_id";
    private static final String DB_CARD_NUMBER = "credit_card_number";
    private static final String DB_CARD_EXPIRATION_DATE = "expiration_date";
    private static final String DB_CARD_OWNER = "owner";
    private static final String DB_CARD_STATE = "description";
    private static final String DB_ACC_ID = "account_id";
    private static final String DB_ACC_NUMBER = "number";
    private static final String DB_ACC_AMOUNT = "amount";
    private static final String DB_ACC_OPENING_DATE = "opening_date";
    private static final String DB_ACC_CURRENCY = "currency";
    private static final String DB_ACC_STATE = "description";
    private static final String DB_OPERATION_ID = "operation_id";
    private static final String DB_OPERATION_DATE = "operation_date";
    private static final String DB_OPERATION_ALLOWED = "allowed";
    private static final String DB_OPERATION_NAME = "operation_name";
    private static final String DB_OPERATION_DETAILS_ID = "details_id";
    private static final String DB_OPERATION_AMOUNT = "amount";
    private static final String DB_OPERATION_CURRENCY = "currency";
    private static final String DB_OPERATION_DESTINATION_ACC = "destination_account_number";

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String SELECT_USER_CARD = "SELECT DISTINCT cards.credit_card_id, cards.credit_card_number, " +
            "cards.expiration_date, cards.owner, state.description FROM users " +
            "INNER JOIN accounts ON users.id=accounts.users_id " +
            "INNER JOIN accounts_has_credit_cards AS a_has_c ON accounts.account_id=a_has_c.account_id " +
            "INNER JOIN credit_cards AS cards ON a_has_c.credit_card_id=cards.credit_card_id " +
            "INNER JOIN credit_card_states AS state ON cards.state_code=state.state_code " +
            "WHERE users.id=?";

    private static final String SELECT_USER_ACCOUNTS_BY_CARD = "SELECT DISTINCT a.account_id, a.number, a.amount, " +
            "a.opening_date, c.currency, a_s.description FROM credit_cards AS credit_c " +
            "INNER JOIN accounts_has_credit_cards AS acc_card ON credit_c.credit_card_id=acc_card.credit_card_id " +
            "INNER JOIN accounts AS a ON acc_card.account_id=a.account_id " +
            "INNER JOIN account_states AS a_s ON a.state_code=a_s.state_code " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id " +
            "WHERE credit_c.credit_card_id=?";

    private static final String SELECT_USER_ACCOUNT = "SELECT DISTINCT a.account_id, a.number, a.amount, " +
            "a.opening_date, c.currency, a_s.description FROM users " +
            "INNER JOIN accounts AS a ON users.id=a.users_id " +
            "INNER JOIN account_states AS a_s ON a.state_code=a_s.state_code " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id " +
            "WHERE users.id=?";

    private static final String SELECT_OPERATIONS = "SELECT DISTINCT o.operation_id, o.operation_date, o.allowed, " +
            "ot.operation_name, od.details_id, od.amount, c.currency, od.destination_account_number " +
            "FROM operations AS o " +
            "INNER JOIN operation_types AS ot ON o.operation_type=ot.operation_code " +
            "INNER JOIN operation_details AS od ON o.operation_id=od.details_id " +
            "INNER JOIN accounts AS a ON o.account_id=a.account_id " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id";

    private static final String WHERE_CARD = " WHERE o.credit_card_id=?";
    private static final String WHERE_ACCOUNT = " WHERE o.account_id=?";
    private static final String ORDER_BY_OP_DATE = " ORDER BY o.operation_date DESC";

    private static final String WRONG_CARD_DATA_INPUT = "wrong data for getting card details";
    private static final String WRONG_ACC_DATA_INPUT = "wrong data for getting acc details";

    private static final String LOG_CONNECTION_POOL_ERROR = "Connection pool error";
    private static final String LOG_ERROR_CLOSING_RESOURCES = "Error closing resources";
    private static final String LOG_ERROR_FETCHING_DATA = "DB error during fetching user data";
    private static final String LOG_GET_CARD_START = "card with id ";
    private static final String LOG_GET_CARD_END = " wasn't found for user ";
    private static final String LOG_GET_ACC_START = "account with id ";
    private static final String LOG_GET_ACC_END = " wasn't found for user ";

    @Override
    public List<Card> getUserCards(int userId) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Card> userCards = new ArrayList<>();

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_USER_CARD);
            ps.setInt(1, userId);
            resultSet = ps.executeQuery();

            Card card = null;

            while (resultSet.next()) {
                int cardId = resultSet.getInt(DB_CARD_ID);
                card = getCard(cardId, resultSet);
                userCards.add(card);
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

        return userCards;
    }

    @Override
    public Card getCardById(int cardId, int userId) throws DAOException {
        List<Card> userCards = getUserCards(userId);
        Card userCard = null;

        for (Card card : userCards) {
            if (card.getCardId() == cardId) {
                userCard = card;
            }
        }
        if (userCard == null) {
            logger.info(LOG_GET_CARD_START + cardId + LOG_GET_CARD_END + userId);
            throw new WrongDataDAOException(WRONG_CARD_DATA_INPUT);
        }

        return userCard;
    }

    @Override
    public List<Account> getAccounts(int id, String destination) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Account> userAccounts = new ArrayList<>();

        String query;
        if (DEST_CARD.equals(destination)) {
            query = SELECT_USER_ACCOUNTS_BY_CARD;
        } else {
            query = SELECT_USER_ACCOUNT;
        }

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            resultSet = ps.executeQuery();

            Account account;
            int accountId;

            while (resultSet.next()) {
                accountId = resultSet.getInt(DB_ACC_ID);
                account = getAcc(accountId, resultSet);
                userAccounts.add(account);
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

        return userAccounts;
    }

    @Override
    public Account getAccById(int id, int userId) throws DAOException {

        List<Account> userAccounts = getAccounts(userId, DEST_ACC);
        Account userAcc = null;

        for (Account acc : userAccounts) {
            if (acc.getAccountId() == id) {
                userAcc = acc;
            }
        }
        if (userAcc == null) {
            logger.info(LOG_GET_ACC_START + id + LOG_GET_ACC_END + userId);
            throw new WrongDataDAOException(WRONG_ACC_DATA_INPUT);
        }

        return userAcc;
    }


    @Override
    public List<Operation> getOperations(int id, String destination) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Operation> operations = new ArrayList<>();

        String query = getOperationQueryByDestination(destination);

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            resultSet = ps.executeQuery();

            Operation operation;
            OperationDetail operationDetail;

            while (resultSet.next()) {

                operation = new Operation();
                operation.setId(resultSet.getInt(DB_OPERATION_ID));
                operation.setDate(resultSet.getTimestamp(DB_OPERATION_DATE));
                operation.setAllowed(resultSet.getBoolean(DB_OPERATION_ALLOWED));
                operation.setOperationName(resultSet.getString(DB_OPERATION_NAME));

                int detailsId = resultSet.getInt(DB_OPERATION_DETAILS_ID);
                if (detailsId != 0) {
                    operationDetail = new OperationDetail();
                    operationDetail.setDetailId(detailsId);

                    operationDetail.setAmount(resultSet.getLong(DB_OPERATION_AMOUNT));
                    operationDetail.setCurrency(resultSet.getString(DB_OPERATION_CURRENCY));
                    operationDetail.setDestinationAccount(resultSet.getString(DB_OPERATION_DESTINATION_ACC));
                    operation.setDetail(operationDetail);
                }

                operations.add(operation);
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

        return operations;
    }

    private Card getCard(int cardId, ResultSet resultSet) throws SQLException {

        String cardNumber = resultSet.getString(DB_CARD_NUMBER);
        LocalDate expirationDate = resultSet.getDate(DB_CARD_EXPIRATION_DATE).toLocalDate();
        String cardOwner = resultSet.getString(DB_CARD_OWNER);
        String cardState = resultSet.getString(DB_CARD_STATE);

        Card card = new Card();
        card.setCardId(cardId);
        card.setCardNumber(cardNumber);
        card.setExpirationDate(expirationDate);
        card.setOwner(cardOwner);
        card.setState(cardState);

        return card;
    }

    private Account getAcc(int accountId, ResultSet resultSet) throws SQLException {

        String accNumber = resultSet.getString(DB_ACC_NUMBER);
        long amount = resultSet.getLong(DB_ACC_AMOUNT);
        LocalDate openingDate = resultSet.getDate(DB_ACC_OPENING_DATE).toLocalDate();
        String currency = resultSet.getString(DB_ACC_CURRENCY);
        String state = resultSet.getString(DB_ACC_STATE);

        Account account = new Account();
        account.setAccountId(accountId);
        account.setAccNumber(accNumber);
        account.setOpeningDate(openingDate);
        account.setAmount(amount);
        account.setCurrency(currency);
        account.setState(state);

        return account;
    }


    /**
     * Gets sql query for receiving performed operation's data based on the destination
     *
     * @param destination is a {@code String} value of who will receive operation's data
     * @return {@code String} value of the select query
     */
    private String getOperationQueryByDestination(String destination) {
        String query = SELECT_OPERATIONS;

        if (DEST_CARD.equals(destination)) {
            query += WHERE_CARD;
        } else {
            query += WHERE_ACCOUNT;
        }
        query += ORDER_BY_OP_DATE;

        return query;
    }

    private void closeResources(Connection con, PreparedStatement ps, ResultSet resultSet) throws DAOException {
        try {
            connectionPool.closeConnection(con, ps, resultSet);
        } catch (ConnectionPoolException e) {
            logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
            throw new DAOException(e);
        }
    }
}
package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.*;
import by.epamtc.zarutski.dao.FacilitiesDAO;
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

public class SQLFacilitiesDAOImpl implements FacilitiesDAO {

    private static final Logger logger = LogManager.getLogger(SQLFacilitiesDAOImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public static final String DEST_CARD = "card";
    public static final String DEST_ACC = "account";
    public static final String DB_CARD_ID = "credit_card_id";
    public static final String DB_CARD_NUMBER = "credit_card_number";
    public static final String DB_EXPIRATION_DATE = "expiration_date";
    public static final String DB_OWNER = "owner";
    public static final String DB_CARD_STATE = "description";
    public static final String DB_ACC_ID = "account_id";
    public static final String DB_ACC_NUMBER = "number";
    public static final String DB_ACC_AMOUNT = "amount";
    public static final String DB_ACC_OPENING_DATE = "opening_date";
    public static final String DB_ACC_CURRENCY = "currency";
    public static final String DB_ACC_STATE = "description";
    public static final String DB_OPERATION_ID = "operation_id";
    public static final String DB_OPERATION_DATE = "operation_date";
    public static final String DB_OPERATION_ALLOWED = "allowed";
    public static final String DB_OPERATION_NAME = "operation_name";
    public static final String DB_OPERATION_DETAILS_ID = "details_id";
    public static final String DB_OPERATION_AMOUNT = "amount";
    public static final String DB_OPERATION_CURRENCY = "currency";
    public static final String DB_OPERATION_DESTINATION_ACC = "destination_account_number";

    private static final String SELECT_USER_CARD = "SELECT DISTINCT cards.credit_card_id, cards.credit_card_number, " +
            "cards.expiration_date, cards.owner, state.description FROM users " +
            "INNER JOIN accounts ON users.id=accounts.users_id " +
            "INNER JOIN accounts_has_credit_cards AS a_has_c ON accounts.account_id=a_has_c.account_id " +
            "INNER JOIN credit_cards AS cards ON a_has_c.credit_card_id=cards.credit_card_id " +
            "INNER JOIN credit_card_states AS state ON cards.state_code=state.state_code " +
            "WHERE users.id=?";

    private static final String SELECT_USER_ACCOUNT = "SELECT DISTINCT a.account_id, a.number, a.amount, " +
            "a.opening_date, c.currency, a_s.description FROM users " +
            "INNER JOIN accounts AS a ON users.id=a.users_id " +
            "INNER JOIN account_states AS a_s ON a.state_code=a_s.state_code " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id " +
            "WHERE users.id=?";

    private static final String SELECT_USER_ACCOUNTS_BY_CARD = "SELECT DISTINCT a.account_id, a.number, a.amount, " +
            "a.opening_date, c.currency, a_s.description FROM credit_cards AS credit_c " +
            "INNER JOIN accounts_has_credit_cards AS acc_card ON credit_c.credit_card_id=acc_card.credit_card_id " +
            "INNER JOIN accounts AS a ON acc_card.account_id=a.account_id " +
            "INNER JOIN account_states AS a_s ON a.state_code=a_s.state_code " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id " +
            "WHERE credit_c.credit_card_id=?";

    private static final String SELECT_OPERATIONS = "SELECT DISTINCT o.operation_id, o.operation_date, o.allowed, " +
            "ot.operation_name, od.details_id, od.amount, c.currency, od.destination_account_number " +
            "FROM operations AS o " +
            "INNER JOIN operation_types AS ot ON o.operation_type=ot.operation_code " +
            "INNER JOIN operation_details AS od ON o.operation_id=od.details_id " +
            "INNER JOIN accounts AS a ON o.account_id=a.account_id " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id";

    public static final String WHERE_CARD = " WHERE o.credit_card_id=?";
    public static final String WHERE_ACCOUNT = " WHERE o.account_id=?";
    public static final String ORDER_BY_OP_DATE = " ORDER BY o.operation_date DESC";

    private static final String LOG_CONNECTION_POOL_ERROR = "Connection pool error";
    private static final String LOG_ERROR_CLOSING_RESOURCES = "Error closing resources";
    private static final String LOG_ERROR_FETCHING_DATA = "DB error during fetching user data";

    public static final String LOG_GET_CARD_START = "card with id ";
    public static final String LOG_GET_CARD_END = " wasn't found for user ";
    public static final String LOG_GET_ACC_START = "account with id ";
    public static final String LOG_GET_ACC_END = " wasn't found for user ";
    public static final String WRONG_CARD_DATA_INPUT = "wrong data for getting card details";
    public static final String WRONG_ACC_DATA_INPUT = "wrong data for getting acc details";


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

    private Card getCard(int cardId, ResultSet resultSet) throws SQLException {
        Card card = null;
        String cardNumber = resultSet.getString(DB_CARD_NUMBER);
        LocalDate expirationDate = resultSet.getDate(DB_EXPIRATION_DATE).toLocalDate();
        String cardOwner = resultSet.getString(DB_OWNER);
        String cardState = resultSet.getString(DB_CARD_STATE);

        card = new Card();
        card.setCardId(cardId);
        card.setCardNumber(cardNumber);
        card.setExpirationDate(expirationDate);
        card.setOwner(cardOwner);
        card.setState(cardState);

        return card;
    }


    @Override
    public List<Account> getAccounts(int id, String destination) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Account> userAccounts = new ArrayList<>();

        String query;
        if (destination.equals(DEST_CARD)) {
            query = SELECT_USER_ACCOUNTS_BY_CARD;
        } else {
            query = SELECT_USER_ACCOUNT;
        }

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, id);

            resultSet = ps.executeQuery();

            Account account = null;
            while (resultSet.next()) {

                int accountId = resultSet.getInt(DB_ACC_ID);
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

    private Account getAcc(int accountId, ResultSet resultSet) throws SQLException {
        Account account = null;

        String accNumber = resultSet.getString(DB_ACC_NUMBER);
        long amount = resultSet.getLong(DB_ACC_AMOUNT); // ВОПРОС
        LocalDate openingDate = resultSet.getDate(DB_ACC_OPENING_DATE).toLocalDate();
        String currency = resultSet.getString(DB_ACC_CURRENCY);
        String state = resultSet.getString(DB_ACC_STATE);

        account = new Account();
        account.setAccountId(accountId);
        account.setAccNumber(accNumber);
        account.setOpeningDate(openingDate);
        account.setAmount(amount);
        account.setCurrency(currency);
        account.setState(state);

        return account;
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

            Operation operation = null;
            OperationDetail operationDetail = null;
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

    private String getOperationQueryByDestination(String destination) {
        String query = SELECT_OPERATIONS;

        if (destination.equals(DEST_CARD)) {
            query += WHERE_CARD;
        } else {
            query += WHERE_ACCOUNT;
        }
        query += ORDER_BY_OP_DATE;

        return query;
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
}
package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.*;
import by.epamtc.zarutski.dao.FacilityActionDAO;
import by.epamtc.zarutski.dao.connection.ConnectionPool;
import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;
import by.epamtc.zarutski.dao.exception.ConfirmationDAOException;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

public class FacilityActionDAOImpl implements FacilityActionDAO {

    private static final Logger logger = LogManager.getLogger(FacilityActionDAOImpl.class);

    private static final String DB_ACC_ID = "account_id";
    private static final String DB_ACC_NUMBER = "number";
    private static final String DB_ACC_AMOUNT = "amount";
    private static final String DB_ACC_OPENING_DATE = "opening_date";
    private static final String DB_ACC_CURRENCY = "currency";
    private static final String DB_ACC_STATE = "description";
    private static final String DB_CARD_ID = "credit_card_id";
    private static final String DB_CARD_CVV_CODE = "cvv_code";

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String SELECT_ACCOUNT_NUM_BY_CARD_NUM = "SELECT a.number, c.currency FROM credit_cards AS credit_c " +
            "INNER JOIN accounts_has_credit_cards AS acc_card " +
            "ON credit_c.credit_card_id=acc_card.credit_card_id " +
            "INNER JOIN accounts AS a ON acc_card.account_id=a.account_id " +
            "INNER JOIN account_states AS a_s ON a.state_code=a_s.state_code " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id " +
            "WHERE credit_c.credit_card_number=?";

    private static final String SELECT_USER_ACCOUNT = "SELECT DISTINCT a.account_id, a.number, a.amount, " +
            "a.opening_date, c.currency, a_s.description FROM users " +
            "INNER JOIN accounts AS a ON users.id=a.users_id " +
            "INNER JOIN account_states AS a_s ON a.state_code=a_s.state_code " +
            "INNER JOIN currences AS c ON a.currences_id=c.currences_id";

    private static final String WHERE_ACC_NUMBER = " WHERE a.number=?";

    private static final String SELECT_CARD_ID_BY_ACC_ID = "SELECT cc.credit_card_id FROM accounts AS a " +
            "INNER JOIN accounts_has_credit_cards AS a_h_c " +
            "ON a.account_id=a_h_c.account_id " +
            "INNER JOIN credit_cards as cc " +
            " ON a_h_c.credit_card_id=cc.credit_card_id " +
            "WHERE a.account_id=?";

    private static final String SELECT_CONFIRMATION_CODE = "SELECT cvv_code FROM credit_cards WHERE credit_card_id = ?";

    private static final String INSERT_INTO_OPERATIONS = "INSERT INTO operations " +
            "(`operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) " +
            "VALUES (?, ?, ?, ?, ?);";

    private static final String INSERT_INTO_OPERATION_DETAILS = "INSERT INTO operation_details " +
            "(`details_id`, `amount`, `destination_account_number`, `operations_operation_id`) " +
            "VALUES (?, ?, ?, ?);";

    private static final String UPDATE_ACC_AMOUNT = "UPDATE accounts SET amount = ?  WHERE (account_id = ?)";

    private static final String INSERT_INTO_OPERATION_DETAILS_REFILL = "INSERT INTO operation_details " +
            "(`details_id`, `amount`, `operations_operation_id`) " +
            "VALUES (?, ?, ?);";

    private static final String INSERT_INTO_ACCOUNTS = "INSERT INTO accounts " +
            "(`number`, `amount`, `opening_date`, `currences_id`, `state_code`, `users_id`) " +
            "VALUES (?, ?, ?, ?, ?, ?);";

    private static final String INSERT_INTO_CREDIT_CARDS = "INSERT INTO credit_cards " +
            "(`credit_card_number`, `expiration_date`, `owner`, `cvv_code`, `state_code`) " +
            "VALUES (?, ?, ?, ?, ?);";

    private static final String INSERT_ACC_HAS_CARDS = "INSERT INTO accounts_has_credit_cards " +
            "(`account_id`, `credit_card_id`) " +
            "VALUES (?, ?);";

    private static final String CHANGE_CARD_STATE = "UPDATE credit_cards SET state_code = ? WHERE credit_card_id = ?";

    private static final int ALLOWED = 1;
    private static final int TRANSFER = 2;
    private static final int REFILL = 1;

    private static final int INITIAL_AMOUNT = 0;
    private static final int INITIAL_STATE_CODE = 1;

    private static final String TRANSFER_FROM_CARD = "card";

    private static final String INSERTION_ERROR_MESSAGE = "DB insertion failed";
    private static final String KEY_ERROR_MESSAGE = "Key generation failed";

    private static final String LOG_CONFIRMATION_FAILED = "confirmation codes doesn't match";
    private static final String LOG_KEYS_GENERATION = "No keys were generated";
    private static final String LOG_SUCCESSFUL = "New card order transaction finished successfully";
    private static final String LOG_DB_ORDER_CARD_ERROR = "DB error during card order";
    private static final String LOG_WRONG_CARD_STATE = "wrong card state";
    private static final String LOG_CONNECTION_POOL_ERROR = "Connection pool error";
    private static final String LOG_ERROR_CLOSING_RESOURCES = "Error closing resources";
    private static final String LOG_ERROR_FETCHING_DATA = "DB error during fetching user data";
    private static final String LOG_DB_TRANSFER_ERROR = "DB error during transfer";
    private static final String LOG_WRONG_ORDER_DATA = "wrong card order data";

    @Override
    public boolean transfer(TransferData transferData) throws DAOException {

        boolean transferSuccessful = false;

        // transfer operation data
        int senderAccountId = transferData.getSenderAccountId();
        long senderAccAmount = transferData.getSenderAccAmount();
        long transferAmount = transferData.getTransferAmount();
        long amountAfterTransfer = senderAccAmount - transferAmount;
        String senderCurrency = transferData.getTransferCurrency();
        String destinationNumber = transferData.getDestinationNumber();
        String destinationAccNumber = getDestAccNumber(transferData);
        int senderCardId = getSenderCardId(transferData);
        Timestamp operationDate = new Timestamp(System.currentTimeMillis());


        // destination operation data (if in DB)
        Account destinationAccount = getAccByNumber(destinationAccNumber);
        boolean destinationAccInDB = false;

        int destinationAccId = 0;
        long destinationAccAmount = -1;
        String destAccCurrency = null;
        int destinationCardId = 0;

        if (destinationAccount != null) {
            destinationAccInDB = true;

            destinationAccId = destinationAccount.getAccountId();
            destinationAccAmount = destinationAccount.getAmount();
            destAccCurrency = destinationAccount.getCurrency();
            destinationCardId = getCardIdByAccountId(destinationAccId);
        }


        if (canPerform(destinationAccInDB, senderCurrency, destAccCurrency)) {

            Connection con = null;
            // transfer prepared statements
            PreparedStatement insertTransferStatement = null;
            PreparedStatement insertTransferDetailsStatement = null;
            PreparedStatement updateSenderAccStatement = null;

            // refill prepared statements
            PreparedStatement insertRefillStatement = null;
            PreparedStatement insertRefillDetailsStatement = null;
            PreparedStatement updateDestAccStatement = null;

            try {
                con = connectionPool.takeConnection();
                con.setAutoCommit(false);

                // insert sender operation
                insertTransferStatement = con.prepareStatement(INSERT_INTO_OPERATIONS, Statement.RETURN_GENERATED_KEYS);
                insertTransferStatement.setTimestamp(1, operationDate);
                insertTransferStatement.setInt(2, ALLOWED);
                insertTransferStatement.setInt(3, TRANSFER);
                insertTransferStatement.setInt(4, senderAccountId);
                insertTransferStatement.setInt(5, senderCardId);
                int affectedRows = insertTransferStatement.executeUpdate();
                checkInsertion(affectedRows);

                // get generated key
                int operationTransferId = extractGeneratedId(insertTransferStatement);
                connectionPool.closeStatement(insertTransferStatement);

                // insert sender operation detail
                insertTransferDetailsStatement = con.prepareStatement(INSERT_INTO_OPERATION_DETAILS);
                insertTransferDetailsStatement.setInt(1, operationTransferId);
                insertTransferDetailsStatement.setLong(2, transferAmount);
                insertTransferDetailsStatement.setString(3, destinationNumber);
                insertTransferDetailsStatement.setInt(4, operationTransferId);
                affectedRows = insertTransferDetailsStatement.executeUpdate();
                connectionPool.closeStatement(insertTransferDetailsStatement);
                checkInsertion(affectedRows);

                // update sender acc
                updateSenderAccStatement = con.prepareStatement(UPDATE_ACC_AMOUNT);
                updateSenderAccStatement.setLong(1, amountAfterTransfer);
                updateSenderAccStatement.setInt(2, senderAccountId);
                affectedRows = updateSenderAccStatement.executeUpdate();
                connectionPool.closeStatement(updateSenderAccStatement);
                checkInsertion(affectedRows);

                // performing refill
                if (destinationAccInDB) {
                    checkConfirmationCode(transferData, senderCardId); // THROWS CONFIRMATION_EXC

                    insertRefillStatement = con.prepareStatement(INSERT_INTO_OPERATIONS, Statement.RETURN_GENERATED_KEYS);
                    insertRefillStatement.setTimestamp(1, operationDate);
                    insertRefillStatement.setInt(2, ALLOWED);
                    insertRefillStatement.setInt(3, REFILL);
                    insertRefillStatement.setInt(4, destinationAccId);
                    insertRefillStatement.setInt(5, destinationCardId);
                    affectedRows = insertRefillStatement.executeUpdate();
                    checkInsertion(affectedRows);

                    // get generated key
                    int operationRefillId = extractGeneratedId(insertRefillStatement);
                    connectionPool.closeStatement(insertRefillStatement);

                    // insert recipient operation detail
                    insertRefillDetailsStatement = con.prepareStatement(INSERT_INTO_OPERATION_DETAILS_REFILL);
                    insertRefillDetailsStatement.setInt(1, operationRefillId);
                    insertRefillDetailsStatement.setLong(2, transferAmount);
                    insertRefillDetailsStatement.setInt(3, operationRefillId);
                    affectedRows = insertRefillDetailsStatement.executeUpdate();
                    connectionPool.closeStatement(insertRefillDetailsStatement);
                    checkInsertion(affectedRows);

                    // update destination acc
                    long newDestAccAmount = transferAmount + destinationAccAmount;
                    updateDestAccStatement = con.prepareStatement(UPDATE_ACC_AMOUNT);
                    updateDestAccStatement.setLong(1, newDestAccAmount);
                    updateDestAccStatement.setInt(2, destinationAccId);
                    affectedRows = updateDestAccStatement.executeUpdate();
                    connectionPool.closeStatement(updateDestAccStatement);
                }

                if (affectedRows == 1) {
                    con.commit();
                    transferSuccessful = true;
                }

            } catch (ConnectionPoolException e) {
                logger.error(LOG_CONNECTION_POOL_ERROR, e);
                throw new DAOException(e);
            } catch (SQLException e) {
                logger.error(LOG_DB_TRANSFER_ERROR, e);
                throw new DAOException(e);
            } finally {
                try {
                    rollbackIfFailed(connectionPool, con, transferSuccessful);
                    connectionPool.finishTransaction(con);
                    connectionPool.closeConnection(con);
                } catch (ConnectionPoolException e) {
                    logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
                    throw new DAOException(e);
                }
            }
        }

        return transferSuccessful;
    }

    @Override
    public boolean orderNewCard(CardOrderData cardOrderData, AccOrderData accOrderData) throws DAOException {

        Connection con = null;
        boolean successful = false;

        PreparedStatement insertAccStatement = null;
        PreparedStatement insertCardStatement = null;
        PreparedStatement insertAccHasCardStatement = null;

        String accNumber = accOrderData.getAccNumber();
        Date openingDate = accOrderData.getOpeningDate();
        int currencyCode = accOrderData.getCurrencyCode();
        int userId = accOrderData.getUserId();

        String cardNumber = cardOrderData.getCardNumber();
        Date expirationDate = cardOrderData.getExpirationDate();
        String owner = cardOrderData.getOwner();
        String cvvCode = cardOrderData.getCvvCode();

        try {
            con = connectionPool.takeConnection();
            con.setAutoCommit(false);

            insertAccStatement = con.prepareStatement(INSERT_INTO_ACCOUNTS, Statement.RETURN_GENERATED_KEYS);

            insertAccStatement.setString(1, accNumber);
            insertAccStatement.setLong(2, INITIAL_AMOUNT);
            insertAccStatement.setDate(3, openingDate);
            insertAccStatement.setInt(4, currencyCode);
            insertAccStatement.setInt(5, INITIAL_STATE_CODE);
            insertAccStatement.setInt(6, userId);

            int affectedRows = insertAccStatement.executeUpdate();
            checkInsertion(affectedRows);

            int accId = extractGeneratedId(insertAccStatement);
            connectionPool.closeStatement(insertAccStatement);


            insertCardStatement = con.prepareStatement(INSERT_INTO_CREDIT_CARDS, Statement.RETURN_GENERATED_KEYS);

            insertCardStatement.setString(1, cardNumber);
            insertCardStatement.setDate(2, expirationDate);
            insertCardStatement.setString(3, owner);
            insertCardStatement.setString(4, cvvCode);
            insertCardStatement.setInt(5, INITIAL_STATE_CODE);

            affectedRows = insertCardStatement.executeUpdate();
            checkInsertion(affectedRows);

            int cardId = extractGeneratedId(insertCardStatement);
            connectionPool.closeStatement(insertCardStatement);

            insertAccHasCardStatement = con.prepareStatement(INSERT_ACC_HAS_CARDS);

            insertAccHasCardStatement.setInt(1, accId);
            insertAccHasCardStatement.setInt(2, cardId);

            if (insertAccHasCardStatement.executeUpdate() == 1) {
                con.commit();
                logger.info(LOG_SUCCESSFUL);
                successful = true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error(LOG_WRONG_ORDER_DATA, e);
            throw new WrongDataDAOException(e);
        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_DB_ORDER_CARD_ERROR, e);
            throw new DAOException(e);
        } finally {
            try {
                rollbackIfFailed(connectionPool, con, successful);
                connectionPool.finishTransaction(con);
                connectionPool.closeConnection(con, insertAccHasCardStatement);
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
                throw new DAOException(e);
            }
        }

        return successful;
    }

    @Override
    public void changeCardState(int cardId, int cardState) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(CHANGE_CARD_STATE);

            ps.setInt(1, cardState);
            ps.setInt(2, cardId);

            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error(LOG_WRONG_CARD_STATE, e);
            throw new WrongDataDAOException(e);
        } catch (ConnectionPoolException e) {
            logger.error(LOG_CONNECTION_POOL_ERROR, e);
            throw new DAOException(e);
        } catch (SQLException e) {
            logger.error(LOG_ERROR_FETCHING_DATA, e);
            throw new DAOException(e);
        } finally {
            try {
                connectionPool.closeConnection(con, ps);
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
                throw new DAOException(e);
            }
        }
    }

    // receives destination account number from TransferData
    // если перевод совершается со счёта на счёт, то возвращает значение destinationNumber объекта TransferData
    // если перевод совершается между картами, то значеине destinationNumber объекта TransferData - это номер карты получателя
    // чтобы получить номер подключённого к карте счёта, внутри вызывается метод getAccByCardNumber
    private String getDestAccNumber(TransferData transferData) throws DAOException {

        String receivedNumber = transferData.getDestinationNumber();
        String destNumber = receivedNumber;

        if (transferFromCard(transferData)) {

            String cardCurrency = transferData.getTransferCurrency();
            String cardAccNum = getAccNumberByCardNumber(receivedNumber, cardCurrency);

            if (cardAccNum != null) {
                destNumber = cardAccNum;
            }
        }

        return destNumber;
    }

    // метод getAccByCardNumber находит список подключённых к карте счётов и возвращает номер наиболее подходящего счёта
    // если не будет найден счёт, с нужной валютой - метод вернёт номер первого найденного счёта
    private String getAccNumberByCardNumber(String cardNumber, String currency) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        String accNumber = null;
        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_ACCOUNT_NUM_BY_CARD_NUM);
            ps.setString(1, cardNumber);

            resultSet = ps.executeQuery();

            String fondCurrency = null;
            while (resultSet.next()) {

                if (accNumber == null) {
                    accNumber = resultSet.getString(DB_ACC_NUMBER);
                }

                fondCurrency = resultSet.getString(DB_ACC_CURRENCY);
                if (fondCurrency.equals(currency)) {
                    accNumber = resultSet.getString(DB_ACC_NUMBER);
                }
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

        return accNumber;
    }

    // если трансфер выполняется со счёта, то вызывается метод getCardIdByAccountId чтобы получить id карты по id счёта
    private int getSenderCardId(TransferData transferData) throws DAOException {
        if (transferFromCard(transferData)) {
            return transferData.getSenderCardId();
        } else {
            int accountId = transferData.getSenderAccountId();
            return getCardIdByAccountId(accountId);
        }
    }

    // формирует объект класса Account на основании данных, полученных из select для указанного destinationNumber
    private Account getAccByNumber(String destinationNumber) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        Account account = null;
        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_USER_ACCOUNT + WHERE_ACC_NUMBER);
            ps.setString(1, destinationNumber);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {

                int accountId = resultSet.getInt(DB_ACC_ID);
                String accNumber = resultSet.getString(DB_ACC_NUMBER);
                long amount = resultSet.getLong(DB_ACC_AMOUNT);
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

        return account;
    }

    private boolean transferFromCard(TransferData transferData) {
        String transferFrom = transferData.getTransferFrom();
        return transferFrom.equals(TRANSFER_FROM_CARD);
    }

    // если доступ к счёту имеет несколько карт, метод возвращает id первой карты из выборки
    // это допустимо т.к. валюта, в которой выполняются операции определяется счётом, а не картой
    private int getCardIdByAccountId(int accId) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        int cardId = 0;
        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_CARD_ID_BY_ACC_ID);
            ps.setInt(1, accId);

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                cardId = resultSet.getInt(DB_CARD_ID);
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

        return cardId;
    }

    // возвращает true, если счёт получателя принадлежит стороннему банку (подразумевает использование API другого банка)
    // если счёт получателя пренадлежит банку, метод возвращает true если валюты счетов совпадают
    private boolean canPerform(boolean banksClient, String senderCurrency, String destCurrency) {
        if (!banksClient) {
            return true;
        }

        return senderCurrency.equals(destCurrency);
    }


    private void checkInsertion(int affectedRows) throws SQLException {
        if (affectedRows == 0) {
            throw new SQLException(INSERTION_ERROR_MESSAGE);
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
            logger.error(LOG_KEYS_GENERATION, e);
            throw new DAOException(KEY_ERROR_MESSAGE);
        } finally {
            try {
                connectionPool.closeStatement(ps, generatedKeys);
            } catch (ConnectionPoolException e) {
                logger.error(LOG_ERROR_CLOSING_RESOURCES, e);
                throw new DAOException(e);
            }
        }
        return userDetailsId;
    }

    // если трансфер выполняется с использованием карты, возвращает true
    // при совпадении полученного cvv кода со значением, хранящимся в БД
    private void checkConfirmationCode(TransferData transferData, int senderCardID) throws DAOException {
        if (transferFromCard(transferData)) {
            String confirmationCode = transferData.getConfirmationCode();
            String code = getConfirmationCode(senderCardID);

            if (!confirmationCode.equals(code)) {
                logger.warn(LOG_CONFIRMATION_FAILED);
                throw new ConfirmationDAOException();
            }
        }
    }

    private String getConfirmationCode(int cardId) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        String confirmation = null;
        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_CONFIRMATION_CODE);
            ps.setInt(1, cardId);

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                confirmation = resultSet.getString(DB_CARD_CVV_CODE);
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

        return confirmation;
    }

    private void rollbackIfFailed(ConnectionPool pool, Connection con, boolean succeed) throws ConnectionPoolException {
        if (!succeed) {
            pool.rollback(con);
        }
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
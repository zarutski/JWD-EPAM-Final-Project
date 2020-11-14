package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.AccOrderData;
import by.epamtc.zarutski.bean.CardOrderData;
import by.epamtc.zarutski.bean.TransferData;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.FacilityActionDAO;
import by.epamtc.zarutski.dao.connection.ConnectionPool;
import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;
import com.ibatis.common.jdbc.ScriptRunner;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class FacilityActionDaoTest {

    private static TransferData accTransferData;
    private static CardOrderData cardData;
    private static AccOrderData accData;

    private static final FacilityActionDAO dao = DAOProvider.getInstance().getFacilityActionDAO();
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String REFRESH_TEST_DB_SCRIPT = "refresh_test_db.sql";

    private static final String TRANSFER_SENDER_ACC_NUMBER = "BY10UNBS3135301412345678";
    private static final String TRANSFER_DESTINATION_NUMBER = "BY12UNBS3078301434561278";
    private static final String TRANSFER_CURRENCY = "BYN";
    private static final String TRANSFER_FROM = "acc";
    private static final int TRANSFER_SENDER_ACC_AMOUNT = 1000;
    private static final int TRANSFER_SENDER_ACC_ID = 1;
    private static final int TRANSFER_AMOUNT = 500;

    private static final String CARD_NUMBER_STUB = "1111222233334444";
    private static final String CARD_OWNER = "Andrei Kravchenko";
    private static final String CARD_PAYMENT_SYSTEM = "VISA";
    private static final String CARD_CVV = "000";
    private static final int CARD_VALIDITY_MONTHS = 12;

    private static final String ACC_NUMBER_STUB = "111122223333444455556666";
    private static final int ACC_CURRENCY_CODE = 1;
    private static final int ACC_USER_ID = 1;

    private static final String WRONG_CURRENCY_USD = "USD";
    private static final int WRONG_CARD_STATE = 0;
    private static final int WRONG_USER_ID = 55;
    private static final int WRONG_ACC_ID = 50;
    private static final int EXPECTED_CARD_STATE = 2;
    private static final int CARD_NEW_STATE = 2;
    private static final int CARD_ID = 1;

    private static final String SELECT_CARD_QUERY = "SELECT * FROM test_payment_app.credit_cards " +
            "WHERE credit_card_number='1111222233334444'";
    private static final String SELECT_CARD_STATE = "SELECT state_code FROM test_payment_app.credit_cards " +
            "WHERE credit_card_id=1";

    private static final String COLUMN_CARD_NUMBER = "credit_card_number";
    private static final String COLUMN_STATE_CODE = "state_code";

    @Before
    public void setup() throws ConnectionPoolException, IOException, SQLException {
        Connection connection = connectionPool.takeConnection();

        URL refreshScriptUrl = getClass().getClassLoader().getResource(REFRESH_TEST_DB_SCRIPT);
        File refreshScript = FileUtils.toFile(refreshScriptUrl);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(refreshScript));

        ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
        scriptRunner.runScript(bufferedReader);
        connectionPool.closeConnection(connection);

        LocalDate localDate = LocalDate.now();
        LocalDate openingDate = localDate.plusMonths(CARD_VALIDITY_MONTHS);
        Date cardExpirationDate = Date.valueOf(openingDate);

        accTransferData = new TransferData();
        accTransferData.setSenderAccountId(TRANSFER_SENDER_ACC_ID);
        accTransferData.setSenderAccAmount(TRANSFER_SENDER_ACC_AMOUNT);
        accTransferData.setSenderAccNumber(TRANSFER_SENDER_ACC_NUMBER);
        accTransferData.setTransferAmount(TRANSFER_AMOUNT);
        accTransferData.setTransferCurrency(TRANSFER_CURRENCY);
        accTransferData.setDestinationNumber(TRANSFER_DESTINATION_NUMBER);
        accTransferData.setTransferFrom(TRANSFER_FROM);

        cardData = new CardOrderData();
        cardData.setCardNumber(CARD_NUMBER_STUB);
        cardData.setExpirationDate(cardExpirationDate);
        cardData.setOwner(CARD_OWNER);
        cardData.setCvvCode(CARD_CVV);
        cardData.setPaymentSystem(CARD_PAYMENT_SYSTEM);

        accData = new AccOrderData();
        accData.setUserId(ACC_USER_ID);
        accData.setAccNumber(ACC_NUMBER_STUB);
        accData.setOpeningDate(Date.valueOf(openingDate));
        accData.setCurrencyCode(ACC_CURRENCY_CODE);
    }

    @Test
    public void transferPositive() throws DAOException {
        boolean actual = dao.transfer(accTransferData);
        assertTrue(actual);
    }

    @Test
    public void accTransferNegativeMismatchCurrencies() throws DAOException {
        accTransferData.setTransferCurrency(WRONG_CURRENCY_USD);
        boolean actual = dao.transfer(accTransferData);
        assertFalse(actual);
    }

    @Test(expected = WrongDataDAOException.class)
    public void accTransferNegativeWrongDestination() throws DAOException {
        accTransferData.setSenderAccountId(WRONG_ACC_ID);
        dao.transfer(accTransferData);
    }

    @Test
    public void orderNewCardPositive() throws DAOException, ConnectionPoolException, SQLException {
        dao.orderNewCard(cardData, accData);

        Connection connection = connectionPool.takeConnection();
        Statement statement = connection.createStatement();
        statement.execute(SELECT_CARD_QUERY);
        ResultSet resultSet = statement.getResultSet();

        String actual = null;
        if (resultSet.next()) {
            actual = resultSet.getString(COLUMN_CARD_NUMBER);
        }
        connectionPool.closeConnection(connection, statement, resultSet);

        assertEquals(CARD_NUMBER_STUB, actual);
    }

    @Test(expected = WrongDataDAOException.class)
    public void orderNewCardNegativeWrongData() throws DAOException {
        accData.setUserId(WRONG_USER_ID);
        dao.orderNewCard(cardData, accData);
    }

    @Test
    public void changeCardStatePositive() throws DAOException, ConnectionPoolException, SQLException {
        dao.changeCardState(CARD_ID, CARD_NEW_STATE);

        Connection connection = connectionPool.takeConnection();
        Statement statement = connection.createStatement();
        statement.execute(SELECT_CARD_STATE);
        ResultSet resultSet = statement.getResultSet();

        int actual = WRONG_CARD_STATE;
        if (resultSet.next()) {
            actual = resultSet.getInt(COLUMN_STATE_CODE);
        }
        connectionPool.closeConnection(connection, statement, resultSet);
        assertEquals(EXPECTED_CARD_STATE, actual);
    }

    @Test(expected = WrongDataDAOException.class)
    public void changeCardStateNegativeWrongState() throws DAOException {
        dao.changeCardState(CARD_ID, WRONG_CARD_STATE);
    }

    @AfterClass
    public static void teardown() {
        connectionPool.dispose();
    }
}

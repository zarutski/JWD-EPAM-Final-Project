package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.AccOrderData;
import by.epamtc.zarutski.bean.CardOrderData;
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

import static org.junit.Assert.assertEquals;

public class FacilityActionDaoTest {

    private static ConnectionPool connectionPool;
    private static CardOrderData cardData;
    private static AccOrderData accData;

    private static final FacilityActionDAO dao = DAOProvider.getInstance().getFacilityActionDAO();

    private static final String REFRESH_TEST_DB_SCRIPT = "refresh_test_db.sql";

    private static final String CARD_NUMBER_STUB = "1111222233334444";
    private static final String OWNER = "Andrei Kravchenko";
    private static final String CVV_CODE_STUB = "000";
    private static final String PAYMENT_SYSTEM = "VISA";
    private static final String ACC_NUMBER_STUB = "111122223333444455556666";

    private static final String COLUMN_CARD_NUMBER = "credit_card_number";
    private static final String COLUMN_STATE_CODE = "state_code";

    private static final String SELECT_CARD_QUERY = "SELECT * FROM test_payment_app.credit_cards " +
            "WHERE credit_card_number='1111222233334444'";
    private static final String SELECT_CARD_STATE = "SELECT state_code FROM test_payment_app.credit_cards " +
            "WHERE credit_card_id=1";

    @BeforeClass
    public static void init() {
        connectionPool = ConnectionPool.getInstance();

        LocalDate localDate = LocalDate.now();
        LocalDate openingDate = localDate.plusMonths(12);
        Date cardExpirationDate = Date.valueOf(openingDate);

        cardData = new CardOrderData();
        cardData.setCardNumber(CARD_NUMBER_STUB);
        cardData.setExpirationDate(cardExpirationDate);
        cardData.setOwner(OWNER);
        cardData.setCvvCode(CVV_CODE_STUB);
        cardData.setPaymentSystem(PAYMENT_SYSTEM);

        accData = new AccOrderData();
        accData.setUserId(1);
        accData.setAccNumber(ACC_NUMBER_STUB);
        accData.setOpeningDate(Date.valueOf(openingDate));
        accData.setCurrencyCode(1);
    }

    @Before
    public void setup() throws ConnectionPoolException, IOException, SQLException {
        Connection connection = connectionPool.takeConnection();

        URL refreshScriptUrl = getClass().getClassLoader().getResource(REFRESH_TEST_DB_SCRIPT);
        File refreshScript = FileUtils.toFile(refreshScriptUrl);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(refreshScript));

        ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
        scriptRunner.runScript(bufferedReader);
        connectionPool.closeConnection(connection);
    }

    @Test
    public void changeCardStatePositive() throws DAOException, ConnectionPoolException, SQLException {
        dao.changeCardState(1, 2);

        Connection connection = connectionPool.takeConnection();
        Statement statement = connection.createStatement();
        statement.execute(SELECT_CARD_STATE);
        ResultSet resultSet = statement.getResultSet();

        int expected = 2;
        int actual = 0;
        if (resultSet.next()) {
            actual = resultSet.getInt(COLUMN_STATE_CODE);
        }
        connectionPool.closeConnection(connection, statement, resultSet);
        assertEquals(expected, actual);
    }


    @Test(expected = WrongDataDAOException.class)
    public void changeCardStateNegativeWrongState() throws DAOException, ConnectionPoolException, SQLException {
        dao.changeCardState(1, 3);
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
        accData.setUserId(55);
        dao.orderNewCard(cardData, accData);
    }

    // TODO --- transfer

    @AfterClass
    public static void teardown() {
        connectionPool.dispose();
    }
}

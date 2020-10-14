package by.epamtc.zarutski.dao.impl;

import by.epamtc.zarutski.bean.Card;
import by.epamtc.zarutski.dao.CardDAO;
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

public class SQLCardDAOImpl implements CardDAO {

    private static final Logger logger = LogManager.getLogger(SQLCardDAOImpl.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String LOG_CONNECTION_POOL_ERROR = "Connection pool error";
    private static final String LOG_ERROR_CLOSING_RESOURCES = "Error closing resources";
    private static final String LOG_ERROR_FETCHING_DATA = "DB error during fetching user data";

    private static final String SELECT_USER_CARDS = "SELECT cards.credit_card_id, cards.credit_card_number, " +
            "cards.expiration_date, cards.owner, state.description FROM users " +
            "INNER JOIN accounts ON users.id=accounts.users_id " +
            "INNER JOIN accounts_has_credit_cards AS a_has_c ON accounts.account_id=a_has_c.account_id " +
            "INNER JOIN credit_cards AS cards ON a_has_c.credit_card_id=cards.credit_card_id " +
            "INNER JOIN credit_card_states AS state ON cards.state_code=state.state_code " +
            "WHERE users.id=?";


    @Override
    public List<Card> getUserCards(int userId) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Card> userCards = new ArrayList<>();

        try {
            con = connectionPool.takeConnection();
            ps = con.prepareStatement(SELECT_USER_CARDS);
            ps.setInt(1, userId);

            resultSet = ps.executeQuery();

            Card card = null;
            while (resultSet.next()) {

                int cardId = resultSet.getInt("credit_card_id");
                String cardNumber = resultSet.getString("credit_card_number");
                LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
                String cardOwner = resultSet.getString("owner");
                String cardState = resultSet.getString("description");


                card = new Card();
                card.setCardId(cardId);
                card.setCardNumber(cardNumber);
                card.setExpirationDate(expirationDate);
                card.setOwner(cardOwner);
                card.setState(cardState);
                userCards.add(card);
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

        return userCards;
    }
}

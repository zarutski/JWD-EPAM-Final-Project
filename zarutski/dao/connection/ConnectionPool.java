package by.epamtc.zarutski.dao.connection;

import by.epamtc.zarutski.dao.connection.exception.ConnectionPoolException;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConnectionPool {

    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);

    private static final String LOG_INIT_POOL_ERROR = "Error init connection pool";
    private static final String LOG_POOL_SQL_EXCEPTION = "SQLException in ConnectionPool";
    private static final String LOG_DATABASE_DRIVER_ERROR = "Can't find database driver class";
    private static final String LOG_CLOSING_CONNECTION_ERROR = "Error closing the connection.";
    private static final String LOG_DB_CONNECTING_ERROR = "Error connecting to the data source";
    private static final String LOG_RS_CLOSE_ERROR = "ResultSet isn't closed.";
    private static final String LOG_ST_CLOSE_ERROR = "Statement isn't closed.";
    private static final String LOG_CONN_CLOSE_ERROR = "Connection wasn't returned to the pool.";

    private static final String LOG_ROLLBACK_ERROR = "Transaction rollback error";
    private static final String LOG_AUTO_COMMIT = "Auto-commit error";

    private static final ConnectionPool instance = new ConnectionPool();
    private static final int DEFAULT_POOL_SIZE = 5;

    private BlockingQueue<Connection> connectionQueue;
    private BlockingQueue<Connection> givenAwayConQueue;

    private String driverName;
    private String url;
    private String user;
    private String password;
    private int poolSize;

    private ConnectionPool() {
        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        this.driverName = dbResourceManager.getValue(DBParameter.DB_DRIVER);
        this.url = dbResourceManager.getValue(DBParameter.DB_URL);
        this.user = dbResourceManager.getValue(DBParameter.DB_USER);
        this.password = dbResourceManager.getValue(DBParameter.DB_PASSWORD);
        try {
            this.poolSize = Integer.parseInt(dbResourceManager.getValue(DBParameter.DB_POLL_SIZE));
        } catch (NumberFormatException e) {
            this.poolSize = DEFAULT_POOL_SIZE;
        }

        try {
            initPoolData();
        } catch (ConnectionPoolException e) {
            logger.error(LOG_INIT_POOL_ERROR, e);
        }

    }

    public static ConnectionPool getInstance() {
        return instance;
    }

    public void initPoolData() throws ConnectionPoolException {
        try {

            Class.forName(driverName);
            givenAwayConQueue = new ArrayBlockingQueue<Connection>(poolSize);
            connectionQueue = new ArrayBlockingQueue<Connection>(poolSize);

            for (int i = 0; i < poolSize; i++) {

                Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                PooledConnection pooledConnection = new PooledConnection(connection);
                connectionQueue.add(pooledConnection);
            }

        } catch (SQLException e) {
            throw new ConnectionPoolException(LOG_POOL_SQL_EXCEPTION);
        } catch (ClassNotFoundException e) {
            throw new ConnectionPoolException(LOG_DATABASE_DRIVER_ERROR);
        }
    }

    public void dispose() {
        clearConnectionQueue();
    }

    private void clearConnectionQueue() {
        try {
            closeConnectionsQueue(connectionQueue);
            closeConnectionsQueue(givenAwayConQueue);
        } catch (SQLException e) {
            logger.error(LOG_CLOSING_CONNECTION_ERROR, e);
        }
    }

    public Connection takeConnection() throws ConnectionPoolException {
        Connection connection = null;
        try {
            connection = connectionQueue.take();
            givenAwayConQueue.add(connection);
        } catch (InterruptedException e) {
            logger.error(LOG_DB_CONNECTING_ERROR);
            throw new ConnectionPoolException(e);
        }
        return connection;
    }

    public void closeConnection(Connection con, Statement st, ResultSet rs) throws ConnectionPoolException {
        try {
            rs.close();
        } catch (SQLException e) {
            logger.error(LOG_RS_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }

        try {
            st.close();
        } catch (SQLException e) {
            logger.error(LOG_ST_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }

        try {
            con.close();
        } catch (SQLException e) {
            logger.error(LOG_CONN_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }

    }

    public void closeConnection(Connection con, Statement st) throws ConnectionPoolException {
        try {
            st.close();
        } catch (SQLException e) {
            logger.error(LOG_ST_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }

        try {
            con.close();
        } catch (SQLException e) {
            logger.error(LOG_CONN_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }
    }

    public void closeConnection(Connection con) throws ConnectionPoolException {
        try {
            con.close();
        } catch (SQLException e) {
            logger.error(LOG_CONN_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }
    }

    private void closeConnectionsQueue(BlockingQueue<Connection> queue) throws SQLException {
        Connection connection;
        while ((connection = queue.poll()) != null) {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            ((PooledConnection) connection).reallyClose();
        }
    }
    public void closeStatement(Statement st) throws ConnectionPoolException {
        try {
            st.close();
        } catch (SQLException e) {
            logger.error(LOG_ST_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }
    }

    public void closeStatement(Statement st, ResultSet rs) throws ConnectionPoolException {
        try {
            rs.close();
        } catch (SQLException e) {
            logger.error(LOG_RS_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }

        try {
            st.close();
        } catch (SQLException e) {
            logger.error(LOG_ST_CLOSE_ERROR);
            throw new ConnectionPoolException(e);
        }
    }

    public void rollback(Connection connection) throws ConnectionPoolException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error(LOG_ROLLBACK_ERROR);
            throw new ConnectionPoolException(e);
        }
    }

    public void finishTransaction(Connection connection) throws ConnectionPoolException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(LOG_AUTO_COMMIT);
            throw new ConnectionPoolException(e);
        }
    }

    private class PooledConnection implements Connection {
        private static final String ALLOCATING_CONN_ERROR = "Error allocating connection in the pool";
        private static final String DELETING_CONN_ERROR = "Error deleting connection from the given away connections pool";
        private static final String CLOSING_CLOSED_MESSAGE = "Attempting to close closed";
        private Connection connection;

        public PooledConnection(Connection c) throws SQLException {
            this.connection = c;
            this.connection.setAutoCommit(true);
        }

        public void reallyClose() throws SQLException {
            connection.close();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }

        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return connection.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        @Override
        public void commit() throws SQLException {
            connection.commit();
        }

        @Override
        public void rollback() throws SQLException {
            connection.rollback();
        }

        @Override
        public void close() throws SQLException {
            if (connection.isClosed()) {
                throw new SQLException(CLOSING_CLOSED_MESSAGE);
            }

            if (connection.isReadOnly()) {
                connection.setReadOnly(false);
            }

            if (!givenAwayConQueue.remove(this)) {
                throw new SQLException(DELETING_CONN_ERROR);
            }

            if (!connectionQueue.offer(this)) {
                throw new SQLException(ALLOCATING_CONN_ERROR);
            }
        }

        @Override
        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);
        }

        @Override
        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            connection.clearWarnings();

        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            connection.setTypeMap(map);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);
        }

        @Override
        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            connection.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            connection.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                                  int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                             int resultSetHoldability) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            connection.setClientInfo(name, value);
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            connection.setClientInfo(properties);

        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            connection.setSchema(schema);

        }

        @Override
        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            connection.abort(executor);

        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            connection.setNetworkTimeout(executor, milliseconds);

        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }

    }

}

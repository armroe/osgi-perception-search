package net.yeeyaa.perception.search.base;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.concurrent.Executor;


public abstract class AbstractConnection implements Connection {
    public void setCatalog(String catalog) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getCatalog() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setSchema(String schema) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String nativeSQL(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean getAutoCommit() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void commit() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void rollback() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setTransactionIsolation(int level) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getTransactionIsolation() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void clearWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setHoldability(int holdability) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Savepoint setSavepoint() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getNetworkTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void abort(Executor executor) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isReadOnly() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }
}
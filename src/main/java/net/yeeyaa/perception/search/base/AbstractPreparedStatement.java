package net.yeeyaa.perception.search.base;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;


public abstract class AbstractPreparedStatement implements PreparedStatement {
    public void setPoolable(boolean poolable) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isPoolable() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void closeOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isCloseOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxFieldSize() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setMaxFieldSize(int max) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxRows() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setMaxRows(int max) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getQueryTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void cancel() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void clearWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setCursorName(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean getMoreResults(int current) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getResultSetHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void addBatch(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void clearBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int[] executeBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void addBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getUpdateCount() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean getMoreResults() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getFetchDirection() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setFetchSize(int rows) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getFetchSize() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getResultSetConcurrency() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int executeUpdate() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void clearParameters() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int executeUpdate(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getResultSetType() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }
}

package net.yeeyaa.perception.search.base;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;


public abstract class AbstractResultSetMetaData implements ResultSetMetaData {
    public int getColumnType(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getPrecision(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getScale(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isAutoIncrement(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isCaseSensitive(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isSearchable(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isCurrency(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int isNullable(int column) throws SQLException {
        return 1;
    }

    public boolean isSigned(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getColumnDisplaySize(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isWritable(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isDefinitelyWritable(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getColumnClassName(int column) throws SQLException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getCatalogName(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getSchemaName(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getTableName(int column) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }
}

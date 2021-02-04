package net.yeeyaa.perception.search;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import net.yeeyaa.eight.ITriProcessor;
import net.yeeyaa.perception.search.base.AbstractConnection;
import net.yeeyaa.perception.search.base.AbstractDataSource;
import net.yeeyaa.perception.search.base.AbstractDatabaseMetaData;
import net.yeeyaa.perception.search.base.AbstractPreparedStatement;
import net.yeeyaa.perception.search.base.AbstractResultSet;
import net.yeeyaa.perception.search.base.AbstractResultSetMetaData;
import net.yeeyaa.perception.search.base.AbstractStatement;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SearchDataSource extends AbstractDataSource {
	protected final Logger log;
	protected final String url;
	protected final Object serverid;
	protected final ITriProcessor<Object, String, Object, ITriProcessor<Object, String, Object[], Object>> holder;
	protected final Properties paras;
	
    @Override
    public Connection getConnection() throws SQLException { 
        return new SearchConnection((Properties)paras.clone());
    }

    @Override
	public Connection getConnection(String user, String password) throws SQLException {
    	Properties properties = (Properties)paras.clone();
    	if (user != null) properties.put("user", user);
    	if (password != null) properties.put("password", password);
    	return new SearchConnection(properties);
	}

	public SearchDataSource(Properties properties) throws SQLException {
        if (!(properties.get("holder") instanceof ITriProcessor) || properties.get("url") == null) throw new SQLException("cannot create datasource");
        else {
        	holder = (ITriProcessor<Object, String, Object, ITriProcessor<Object, String, Object[], Object>>) properties.remove("holder");
        	url = properties.remove("url").toString();
        	log = properties.get("log") instanceof Logger ? (Logger) properties.remove("log") : LoggerFactory.getLogger(SearchDataSource.class);
        	serverid = properties.get("serverid") == null ? getClass().getName() + "." + UUID.randomUUID() : properties.remove("serverid");
        	paras = properties;
        }
    }
    
    public class SearchConnection extends AbstractConnection {
    	protected final UUID id = UUID.randomUUID();
    	protected volatile boolean isClosed = false;
    	protected volatile Properties clientInfo;
    	protected Connection connection;
    	
    	public SearchConnection(Properties properties) throws SQLException {
    		clientInfo = properties;
    		ITriProcessor<Object, String, Object[], Object> config = holder.operate(serverid, "search", clientInfo.clone());
    		if (config != null) {
	        	Object info = config.operate(id, "schema", null);
	            if (info instanceof Map) clientInfo.putAll((Map<Object, Object>) info);
    		}
    		connection = DriverManager.getConnection(url, clientInfo);
    	}
    	
        public class SearchResultSet extends AbstractResultSet {
        	protected volatile boolean isClosed = false;
            protected volatile boolean wasNull = false;
            protected final Entry<String, String>[] cols;
            protected final Iterator<Map<String, Object>> itr;
            protected Map<String, Object> row;

            protected SearchResultSet(String name) throws SQLException {
        		ITriProcessor<Object, String, Object[], Object> config = holder.operate(serverid, "search", clientInfo.clone());
        		if (config == null) throw new SQLException("cannot get schema!");
        		Map<String, String> cs = (Map<String, String>) config.operate(id, "type", name == null ? null : new Object[]{name});
        		if (cs == null) throw new SQLException("cannot get cols!");
        		cols = cs.entrySet().toArray(new Entry[cs.size()]);
        		Collection<Map<String, Object>> rows = (Collection<Map<String, Object>>) config.operate(id, "meta", name == null ? null : new Object[]{name});
        		if (rows == null) throw new SQLException("cannot get rows!");
        		itr = rows.iterator();
            }

            public boolean next() throws SQLException {
                if (isClosed) throw new SQLException("Resultset is closed");
                else if (itr.hasNext()) {
                    row = itr.next();
                    return true;
                } else return false;
            }

            public boolean isClosed() throws SQLException {
                return isClosed;
            }

            public void close() throws SQLException {
                row = null;
                isClosed = true;
        		ITriProcessor<Object, String, Object[], Object> config = holder.operate(serverid, "search", clientInfo.clone());
        		if (config != null) config.operate(id, "close", null);
            }

            public boolean wasNull() throws SQLException {
                return wasNull;
            }

            public ResultSetMetaData getMetaData() throws SQLException {
                if (isClosed) throw new SQLException("Resultset is closed");
                else return new AbstractResultSetMetaData(){
                    public int getColumnCount() throws SQLException {
                        return cols.length;
                    }

                    public String getColumnName(int column) throws SQLException {
                        return cols[toZeroIndex(column)].getKey();
                    }

                    public String getColumnLabel(int column) throws SQLException {
                        return getColumnName(column);
                    }

                    public String getColumnTypeName(int column) throws SQLException {
                        return cols[toZeroIndex(column)].getValue();
                    }

                    public boolean isReadOnly(int column) throws SQLException {
                        return true;
                    }

                    protected int toZeroIndex(int column) throws SQLException {
                        if (column >= 1 && column <= cols.length) return column - 1;
                        else throw new SQLException("Invalid column value: " + column);
                    }
                };
            }

            protected String findColumnNameIndex(int columnIndex) throws SQLException {
                if (row != null) if (columnIndex >= 1 && columnIndex <= cols.length) return cols[columnIndex - 1].getKey();
                else throw new SQLException("Invalid columnIndex: " + columnIndex);
                else throw new SQLException("No row found.");
            }

            public Object getObject(String columnLabel) throws SQLException {
                if (StringUtils.isBlank(columnLabel))  throw new SQLException("columnName is null.");
                else if (row == null) throw new SQLException("No row found.");
                else if (row.size() == 0) throw new SQLException("RowSet does not contain any columns!");
                else if (row.containsKey(columnLabel)) {
                    Object result = row.get(columnLabel);
                    wasNull = result == null;
                    return result;
                } else throw new SQLException("Could not find " + columnLabel + " in row");
            }

            public Object getObject(int columnIndex) throws SQLException {
                return getObject(findColumnNameIndex(columnIndex));
            }

            public String getString(String columnLabel) throws SQLException {
                Object value = getObject(columnLabel);
                if (wasNull) return null;
                else try {
                    return value instanceof byte[] ? new String((byte[])((byte[])value), "UTF-8") : value.toString();
                } catch (UnsupportedEncodingException e) {
                	return value instanceof byte[] ? new String((byte[])((byte[])value)) : value.toString();
                }
            }

            public String getString(int columnIndex) throws SQLException {
                return getString(findColumnNameIndex(columnIndex));
            }

            public int getInt(String columnLabel) throws SQLException {
                try {
                    Object obj = getObject(columnLabel);
                    if (obj instanceof Number) return ((Number)obj).intValue();
                    else if (obj == null) return 0;
                    else if (obj instanceof String) return Integer.parseInt((String)obj);
                    else throw new Exception("Illegal conversion");
                } catch (Exception e) {
                    throw new SQLException("Cannot convert column " + columnLabel + " to integer", e);
                }
            }

            public int getInt(int columnIndex) throws SQLException {
                return getInt(findColumnNameIndex(columnIndex));
            }

            public long getLong(String columnLabel) throws SQLException {
                try {
                    Object obj = getObject(columnLabel);
                    if (obj instanceof Number) return ((Number)obj).longValue();
                    else if (obj == null) return 0L;
                    else if (obj instanceof String) return Long.parseLong((String)obj);
                    else throw new Exception("Illegal conversion");
                } catch (Exception e) {
                    throw new SQLException("Cannot convert column " + columnLabel + " to long", e);
                }
            }

            public long getLong(int columnIndex) throws SQLException {
                return getLong(findColumnNameIndex(columnIndex));
            }

            public short getShort(String columnLabel) throws SQLException {
                try {
                    Object obj = getObject(columnLabel);
                    if (obj instanceof Number) return ((Number)obj).shortValue();
                    else if (obj == null) return 0;
                    else if (obj instanceof String) return Short.parseShort((String)obj);
                    else throw new Exception("Illegal conversion");
                } catch (Exception e) {
                    throw new SQLException("Cannot convert column " + columnLabel + " to short", e);
                }
            }

            public short getShort(int columnIndex) throws SQLException {
                return getShort(findColumnNameIndex(columnIndex));
            }

            public boolean getBoolean(String columnLabel) throws SQLException {
                Object obj = getObject(columnLabel);
                if (obj instanceof Boolean) return ((Boolean)obj).booleanValue();
                else if (obj == null) return false;
                else if (obj instanceof Number) return ((Number)obj).intValue() != 0;
                else if (obj instanceof String) return !"0".equals(obj);
                else throw new SQLException("Cannot convert column " + columnLabel + " to boolean");
            }

            public boolean getBoolean(int columnIndex) throws SQLException {
                return getBoolean(findColumnNameIndex(columnIndex));
            }

            public byte getByte(String columnLabel) throws SQLException {
                Object obj = getObject(columnLabel);
                if (obj instanceof Number) return ((Number)obj).byteValue();
                else if (obj == null) return 0;
                else throw new SQLException("Cannot convert column " + columnLabel + " to byte");
            }

            public byte getByte(int columnIndex) throws SQLException {
                return getByte(findColumnNameIndex(columnIndex));
            }

            public float getFloat(String columnLabel) throws SQLException {
                try {
                    Object obj = getObject(columnLabel);
                    if (obj instanceof Number) return ((Number)obj).floatValue();
                    else if (obj == null) return 0.0F;
                    else if (obj instanceof String) return Float.parseFloat((String)obj);
                    else throw new Exception("Illegal conversion");
                } catch (Exception e) {
                    throw new SQLException("Cannot convert column " + columnLabel + " to float", e);
                }
            }

            public float getFloat(int columnIndex) throws SQLException {
                return getFloat(findColumnNameIndex(columnIndex));
            }

            public double getDouble(String columnLabel) throws SQLException {
                try {
                    Object obj = getObject(columnLabel);
                    if (obj instanceof Number) return ((Number)obj).doubleValue();
                    else if (obj == null) return 0.0D;
                    else if (obj instanceof String) return Double.parseDouble((String)obj);
                    else throw new Exception("Illegal conversion");
                } catch (Exception e) {
                    throw new SQLException("Cannot convert column " + columnLabel + " to double", e);
                }
            }

            public double getDouble(int columnIndex) throws SQLException {
                return getDouble(findColumnNameIndex(columnIndex));
            }

            public Date getDate(String columnLabel) throws SQLException {
                Object obj = getObject(columnLabel);
                if (obj == null) return null;
                else if (obj instanceof Date) return (Date)obj;
                else {
                	if (obj instanceof String) try {
                        return Date.valueOf((String)obj);
                    } catch (Exception e) {
                        throw new SQLException("Cannot convert column " + columnLabel + " to date", e);
                    }
                    throw new SQLException("Cannot convert column " + columnLabel + " to date: Illegal conversion");
                }
            }

            public Date getDate(int columnIndex) throws SQLException {
                return getDate(findColumnNameIndex(columnIndex));
            }

            public Timestamp getTimestamp(String columnLabel) throws SQLException {
                Object obj = getObject(columnLabel);
                if (obj == null) return null;
                else if (obj instanceof Timestamp) return (Timestamp)obj;
                else if (obj instanceof String) return Timestamp.valueOf((String)obj);
                else throw new SQLException("Illegal conversion");
            }

            public Timestamp getTimestamp(int columnIndex) throws SQLException {
                return getTimestamp(findColumnNameIndex(columnIndex));
            }

            public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
                Object val = getObject(columnLabel);
                if (val != null && !(val instanceof BigDecimal)) throw new SQLException("Illegal conversion");
                else return (BigDecimal)val;
            }

            public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
                return getBigDecimal(findColumnNameIndex(columnIndex));
            }

            public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
                return getBigDecimal(columnLabel).round(new MathContext(scale));
            }

            public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
                return getBigDecimal(findColumnNameIndex(columnIndex), scale);
            }

            public int getType() throws SQLException {
                return TYPE_FORWARD_ONLY;
            }

            public int getConcurrency() throws SQLException {
                return CONCUR_READ_ONLY;
            }
        }
    	
    	public Statement createStatement() throws SQLException {
    		return new AbstractStatement(){
        	    protected ResultSet resultSet;
        	    protected Boolean isClosed = false;

        	    public ResultSet executeQuery(String sql) throws SQLException {
        	        if (!execute(sql)) throw new SQLException("The query did not generate a result set!");
        	        else return resultSet;
        	    }

        	    public boolean execute(String sql) throws SQLException {
        	        resultSet = connection.createStatement().executeQuery(sql);
        	        return true;
        	    }

        	    public void close() throws SQLException {
        	        if (!isClosed) {
        	            isClosed = true;
        	            if (resultSet != null) try {
        	                resultSet.close();
        	            } finally {
        	                resultSet = null;
        	            }
        	        }
        	    }

        	    public ResultSet getResultSet() throws SQLException {
        	    	if (isClosed) throw new SQLException("Can't getResultSet after statement has been closed");
        	        return resultSet;
        	    }

        	    public Connection getConnection() throws SQLException {
        	        return SearchConnection.this;
        	    }

        	    public boolean isClosed() throws SQLException {
        	        return isClosed;
        	    }		
    		};
    	}

    	public PreparedStatement prepareStatement(final String sql) throws SQLException {
    		return new AbstractPreparedStatement(){
    		    protected final HashMap<Integer, String> parameters = new HashMap<Integer, String>();
    		    protected ResultSet resultSet;
    		    protected boolean isClosed = false;

    		    public ResultSet executeQuery() throws SQLException {
    		        if (!execute(updateSql(sql, parameters))) throw new SQLException("The query did not generate a result set!");
    		        else return resultSet;
    		    }

    		    public void setNull(int parameterIndex, int sqlType) throws SQLException {
    		        parameters.put(parameterIndex, "NULL");
    		    }

    		    public void setInt(int parameterIndex, int x) throws SQLException {
    		        parameters.put(parameterIndex, new Integer(x).toString());
    		    }

    		    public void setLong(int parameterIndex, long x) throws SQLException {
    		        parameters.put(parameterIndex, new Long(x).toString());
    		    }

    		    public void setFloat(int parameterIndex, float x) throws SQLException {
    		        parameters.put(parameterIndex, new Float(x).toString());
    		    }

    		    public void setDouble(int parameterIndex, double x) throws SQLException {
    		        parameters.put(parameterIndex, new Double(x).toString());
    		    }

    		    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    		        parameters.put(parameterIndex, new Boolean(x).toString());
    		    }

    		    public void setByte(int parameterIndex, byte x) throws SQLException {
    		        parameters.put(parameterIndex, new Byte(x).toString());
    		    }

    		    public void setShort(int parameterIndex, short x) throws SQLException {
    		        parameters.put(parameterIndex, new Short(x).toString());
    		    }

    		    protected String replaceBackSlashSingleQuote(String x) {
    		        StringBuilder newX = new StringBuilder();
    		        for(int i = 0; i < x.length(); ++i) {
    		            char c = x.charAt(i);
    		            if (c == '\\' && i < x.length() - 1) {
    		                char c1 = x.charAt(i + 1);
    		                if (c1 == '\'') newX.append(c1);
    		                else {
    		                    newX.append(c);
    		                    newX.append(c1);
    		                }
    		                ++i;
    		            } else newX.append(c);
    		        }

    		        return newX.toString();
    		    }

    		    public void setString(int parameterIndex, String x) throws SQLException {
    		        x = replaceBackSlashSingleQuote(x);
    		        x = x.replace("'", "\\'");
    		        parameters.put(parameterIndex, "'" + x + "'");
    		    }

    		    public void setDate(int parameterIndex, Date x) throws SQLException {
    		        StringBuilder newX = new StringBuilder();
    		        newX.append('\'').append(x).append('\'');
    		        parameters.put(parameterIndex, newX.toString());
    		    }

    		    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    		        StringBuilder newX = new StringBuilder();
    		        newX.append('\'').append(x).append('\'');
    		        parameters.put(parameterIndex, newX.toString());
    		    }

    		    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    		        parameters.put(parameterIndex, x.toString());
    		    }

    		    public void setObject(int parameterIndex, Object x) throws SQLException {
    		        if (x == null) setNull(parameterIndex, 0);
    		        else if (x instanceof String) setString(parameterIndex, (String)x);
    		        else if (x instanceof Short) setShort(parameterIndex, ((Short)x).shortValue());
    		        else if (x instanceof Integer) setInt(parameterIndex, ((Integer)x).intValue());
    		        else if (x instanceof Long) setLong(parameterIndex, ((Long)x).longValue());
    		        else if (x instanceof Float) setFloat(parameterIndex, ((Float)x).floatValue());
    		        else if (x instanceof Double) setDouble(parameterIndex, ((Double)x).doubleValue());
    		        else if (x instanceof Date) setDate(parameterIndex, (Date)x);
    		        else if (x instanceof Boolean) setBoolean(parameterIndex, ((Boolean)x).booleanValue());
    		        else if (x instanceof Byte) setByte(parameterIndex, ((Byte)x).byteValue());
    		        else if (x instanceof Character) setString(parameterIndex, x.toString());
    		        else if (x instanceof Timestamp) setTimestamp(parameterIndex, (Timestamp)x);
    		        else {
    		            if (!(x instanceof BigDecimal)) throw new SQLException(MessageFormat.format("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", x.getClass().getName()));
    		            setString(parameterIndex, x.toString());
    		        }
    		    }

    		    public boolean execute() throws SQLException {
    		        return execute(updateSql(sql, parameters));
    		    }

    		    public ResultSet executeQuery(String sql) throws SQLException {
    		        if (!execute(updateSql(sql, parameters))) throw new SQLException("The query did not generate a result set!");
    		        else return resultSet;
    		    }

    		    protected String updateSql(String sql, HashMap<Integer, String> parameters) throws SQLException {
    		        List<String> parts = new ArrayList<String>();
    		        int apCount = 0;
    		        int off = 0;
    		        boolean skip = false;
    		        for(int i = 0; i < sql.length(); ++i) {
    		            char c = sql.charAt(i);
    		            if (skip) skip = false;
    		            else switch(c) {
    		                case '\'': ++apCount;
    		                    break;
    		                case '?': if ((apCount & 1) == 0) {
    		                        parts.add(sql.substring(off, i));
    		                        off = i + 1;}
    		                    break;
    		                case '\\': skip = true;
    		            }
    		        }
    		        parts.add(sql.substring(off, sql.length()));
    		        StringBuilder newSql = new StringBuilder((String)parts.get(0));
    		        for(int i = 1; i < parts.size(); ++i) {
    		            if (!parameters.containsKey(i)) throw new SQLException("Parameter #" + i + " is unset");
    		            newSql.append((String)parameters.get(i));
    		            newSql.append((String)parts.get(i));
    		        }
    		        return newSql.toString();
    		    }

    		    public void close() throws SQLException {
    		        if (!isClosed) {
    		            isClosed = true;
    		            if (resultSet != null) try {
    		                resultSet.close();
    		            } finally {
    		                resultSet = null;
    		            }
    		        }
    		    }

    		    public boolean execute(String sql) throws SQLException {
    		        resultSet = connection.createStatement().executeQuery(sql);
    		        return true;
    		    }

    		    public ResultSet getResultSet() throws SQLException {
    		    	if (isClosed) throw new SQLException("Can't getResultSet after statement has been closed");
    		        return resultSet;
    		    }

    		    public Connection getConnection() throws SQLException {
    		        return SearchConnection.this;
    		    }

    		    public boolean isClosed() throws SQLException {
    		        return isClosed;
    		    }
    		};
    	}

    	public void close() throws SQLException {
    		if (!isClosed) {
    			isClosed = true;
    			if (connection != null) try{
    				connection.close();
    			} finally {
    				connection = null;
    	    	}
    		}
    	}

    	public boolean isClosed() throws SQLException {
    		return isClosed;
    	}

    	public DatabaseMetaData getMetaData() throws SQLException {
    		if (isClosed) throw new SQLException("Connection is closed");
    		else return new AbstractDatabaseMetaData() {
    		    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
    		        return new SearchResultSet(null);
    		    }

    		    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
    		        return new SearchResultSet(tableNamePattern);
    		    }

    		    public Connection getConnection() throws SQLException {
    		        return SearchConnection.this;
    		    }

    		    public int getSQLStateType() throws SQLException {
    		        return sqlStateSQL;
    		    }
    		};
    	}

    	public boolean isValid(int timeout) throws SQLException {
    		return !isClosed && connection != null;
    	}

    	public void setClientInfo(String name, String value) throws SQLClientInfoException {
    		if (clientInfo == null) clientInfo = new Properties();
    		clientInfo.put(name, value);
    	}

    	public void setClientInfo(Properties properties) throws SQLClientInfoException {
    		clientInfo = properties;
    	}

    	public String getClientInfo(String name) throws SQLException {
    		return clientInfo == null ? null : clientInfo.getProperty(name);
    	}

    	public Properties getClientInfo() throws SQLException {
    		return clientInfo == null ? new Properties() : clientInfo;
    	}
    	
    	public String getSchema() throws SQLException {
    		if (isClosed) throw new SQLException("Connection is closed");
    		else return clientInfo.getProperty("schema");
    	}
    }
}

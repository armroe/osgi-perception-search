package net.yeeyaa.perception.search;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yeeyaa.eight.IBiProcessor;
import net.yeeyaa.eight.IProcessor;
import net.yeeyaa.eight.ITriProcessor;
import net.yeeyaa.eight.PlatformException;
import net.yeeyaa.eight.core.PlatformError;
import net.yeeyaa.eight.core.util.TypeConvertor;


public class Search implements IProcessor<Object, Object>, IBiProcessor<Object, Object[], Object> {
    protected final Logger log;
	protected final InheritableThreadLocal<Map<String, Object>> store = new InheritableThreadLocal<Map<String, Object>>();
	protected volatile ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<String, Connection>();
	protected Object serverid = getClass().getName() + "." + UUID.randomUUID();
	protected ITriProcessor<Object, String, Object, ITriProcessor<Object, String, Object, Object>> holder;
	protected IProcessor<Object, Boolean> checker;
	protected Driver driver;
	protected String url = "jdbc:search:";
	
	public enum Fuction{execute, meta, check}

	public void setChecker(IProcessor<Object, Boolean> checker) {
		this.checker = checker;
	}

	public void setServerid(Object serverid) {
		if (serverid != null) this.serverid = serverid;
	}

	public void setHolder(ITriProcessor<Object, String, Object, ITriProcessor<Object, String, Object, Object>> holder) {
		this.holder = holder;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Search() {
		this.log = LoggerFactory.getLogger(Search.class);
	}

	public Search(Logger log) {
		this.log = log == null ? LoggerFactory.getLogger(Search.class) : log;
	}
	
	@Override
	public Object process(Object object) {
		if (object == null) {
			destroy();
			return null;
		} else {
			Map<String, Object> map = store.get();
			return map == null ? null : map.get(object);
		}
	}

	@PreDestroy
    public synchronized void destroy() {
		ConcurrentHashMap<String, Connection> connections = this.connections;
    	this.connections = new ConcurrentHashMap<String, Connection>();
    	if (connections != null && connections.size() > 0) for (Connection c : connections.values()) try {
			c.close();
		} catch (Exception e) {
        	log.error("close connection fail.", e);
		}
    }
	
	public class Check implements IProcessor<Object[], Boolean> {
		@Override
		public Boolean process(Object[] paras) {
			return checker == null ? true : checker.process(paras[0]);
		}
	}

	public class Meta implements IProcessor<Object[], Object> {
		@Override
		public Object process(Object[] paras) {
			ITriProcessor<Object, String, Object, Object> config = holder.operate(serverid, "search", new LinkedHashMap((Map)paras[0]));
			return config == null ? null : config.operate(serverid, "meta", paras.length > 1 ? paras[1] : null);
		}
	}
	
	public class Execute implements IProcessor<Object[], LinkedList<Object>> {
		@Override
		public LinkedList<Object> process(Object[] paras) {
			try {
				Map<String, Object> parameters = (Map<String, Object>)paras[0];
				store.set(parameters);
				Object schema = parameters.get("schema");
				Connection conn = null;
				if (schema != null) conn = connections.get(schema.toString());
				if (conn == null) {
					ITriProcessor<Object, String, Object, Object> config = holder.operate(serverid, "search", new LinkedHashMap((Map)paras[0]));
					if (config != null) {
						 Object info = config.operate(serverid, "schema", null);
						 if (info instanceof Properties) {
							 Properties clientInfo = new Properties();
							 clientInfo.putAll(parameters);
							 clientInfo.putAll((Properties)info);
							 conn = driver == null ? DriverManager.getConnection(url, clientInfo) : driver.connect(url, clientInfo);
							 if (conn != null && schema != null) connections.put(schema.toString(), conn);
						 }
					}
				}
				ResultSet rs = conn.createStatement().executeQuery(paras[1].toString());
				ResultSetMetaData meta = rs.getMetaData();
				LinkedList<Object> ret = new LinkedList<Object>();
				LinkedHashMap<String, Object> m = new LinkedHashMap<String, Object>();
				for (int i= 1; i <= meta.getColumnCount(); i++) m.put(meta.getColumnLabel(i), meta.getColumnType(i));
				ret.add(m);
				while (rs.next()) {
					Object[] row = new Object[meta.getColumnCount()];
					for (int i= 1; i <= row.length; i++) switch (meta.getColumnType(i)) {
						case Types.DISTINCT:
						case Types.CHAR:
						case Types.VARCHAR:
						case Types.LONGVARCHAR:  row[i - 1] = rs.getString(i);
						break;
						case Types.NCHAR:
						case Types.NVARCHAR:
						case Types.LONGNVARCHAR: row[i - 1] = rs.getNString(i);
						break;
						case Types.ARRAY : row[i - 1] = rs.getArray(i);
						break;
						case Types.BIGINT : row[i - 1] = rs.getLong(i);
						break;
						case Types.STRUCT :
						case Types.OTHER :
						case Types.JAVA_OBJECT :  row[i - 1] = rs.getObject(i);
						break;
						case Types.NCLOB :  row[i - 1] = TypeConvertor.inputToBytes(rs.getNClob(i).getAsciiStream(), 8196, -1L);
						break;
						case Types.VARBINARY:
						case Types.BINARY : row[i - 1] = rs.getBytes(i);
						break;
						case Types.LONGVARBINARY: row[i - 1] = TypeConvertor.inputToBytes(rs.getBinaryStream(i), 8196, -1L);
						break;
						case Types.BOOLEAN:
						case Types.BIT : row[i - 1] = rs.getBoolean(i);
						break;
						case Types.ROWID : row[i - 1] = rs.getRowId(i);
						break;
						case Types.REF : row[i - 1] = rs.getRef(i);
						break;
						case Types.DATALINK : row[i - 1] = rs.getURL(i);
						break;
						case Types.BLOB : row[i - 1] = TypeConvertor.inputToBytes(rs.getBlob(i).getBinaryStream(), 8196, -1L);
						break;
						case Types.CLOB : row[i - 1] = TypeConvertor.inputToBytes(rs.getClob(i).getAsciiStream(), 8196, -1L);
						break;
						case Types.TINYINT : row[i - 1] = rs.getByte(i);
						break;
						case Types.SMALLINT : row[i - 1] = rs.getShort(i);
						break;
						case Types.INTEGER : row[i - 1] = rs.getInt(i);
						break;
						case Types.REAL : row[i - 1] = rs.getFloat(i);
						break;
						case Types.FLOAT :
						case Types.DOUBLE : row[i - 1] = rs.getDouble(i);
						break;
						case Types.NUMERIC :
						case Types.DECIMAL : row[i - 1] = rs.getBigDecimal(i);
						break;
						case Types.DATE : row[i - 1] = rs.getDate(i);
						break;
						case Types.TIME : row[i - 1] = rs.getTime(i);
						break;
						case Types.TIMESTAMP : row[i - 1] = rs.getTimestamp(i);
						break;
						case Types.SQLXML : row[i - 1] = TypeConvertor.inputToBytes(rs.getSQLXML(i).getBinaryStream(), 8196, -1L);
					}
					ret.add(row);
				}
				return ret;
			} catch (PlatformException e) {
				throw e;
			} catch (Exception e) {
				throw new PlatformException(SearchError.ERROR_CONNECTION_FAIL, e);
			}
		}
	}
	
	@Override
	public Object perform(Object method, Object[] paras) {
		if (paras != null && paras.length > 1 && paras[0] instanceof Map) switch (Fuction.valueOf(method.toString())) {
			case check : return checker == null ? true : checker.process(paras[0]);
			case meta :  ITriProcessor<Object, String, Object, Object> conf = holder.operate(serverid, "search", new LinkedHashMap((Map)paras[0]));
    			return conf == null ? null : conf.operate(serverid, "meta", paras.length > 1 ? paras[1] : null);
			default :try {
				Map<String, Object> parameters = (Map<String, Object>)paras[0];
				store.set(parameters);
				Object schema = parameters.get("schema");
				Connection conn = null;
				if (schema != null) conn = connections.get(schema.toString());
				if (conn == null) {
					ITriProcessor<Object, String, Object, Object> config = holder.operate(serverid, "search", new LinkedHashMap((Map)paras[0]));
					if (config != null) {
						 Object info = config.operate(serverid, "schema", null);
						 if (info instanceof Properties) {
							 Properties clientInfo = new Properties();
							 clientInfo.putAll(parameters);
							 clientInfo.putAll((Properties)info);
							 conn = driver == null ? DriverManager.getConnection(url, clientInfo) : driver.connect(url, clientInfo);
							 if (conn != null && schema != null) connections.put(schema.toString(), conn);
						 }
					}
				}
				return conn.createStatement().executeQuery(paras[1].toString());
			} catch (PlatformException e) {
				throw e;
			} catch (Exception e) {
				throw new PlatformException(SearchError.ERROR_CONNECTION_FAIL, e);
			}
		}
		throw new PlatformException(PlatformError.ERROR_PARAMETERS);
	}
}

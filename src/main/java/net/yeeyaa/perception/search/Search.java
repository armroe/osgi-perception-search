package net.yeeyaa.perception.search;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.MemoryHandler;

import javax.sql.DataSource;

import net.yeeyaa.eight.IBiProcessor;
import net.yeeyaa.eight.IProcessor;
import net.yeeyaa.eight.PlatformException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;


public class Search implements IProcessor<Map<Object, Object>, DataSource>, IBiProcessor<String, String, Connection>, Driver {
    protected final Logger log;
    protected final java.util.logging.Logger logger;
    protected volatile DataSource dataSource;
    protected String source;
    protected ClassLoader classloader;
    protected Properties paras;
    protected String prefix = "jdbc:ps:";
    protected int major = 1;
    protected int minor;
    protected boolean jdbcCompliant = true;
    
	public Search() {
		this.log = LoggerFactory.getLogger(Search.class);
		MemoryHandler h = new MemoryHandler(new SLF4JBridgeHandler(log), 1, Level.ALL);
		h.setLevel(Level.ALL);
		this.logger = java.util.logging.Logger.getLogger(UUID.randomUUID().toString());
		this.logger.addHandler(h);
	}

	public Search(Logger log) {
		this.log = log == null ? LoggerFactory.getLogger(Search.class) : log;
		MemoryHandler h = new MemoryHandler(new SLF4JBridgeHandler(log), 1, Level.ALL);
		h.setLevel(Level.ALL);
		this.logger = java.util.logging.Logger.getLogger(UUID.randomUUID().toString());
		this.logger.addHandler(h);
	}
    
	public void setPrefix(String prefix) {
		if (prefix != null) this.prefix = prefix;
	}
 
	public void setMajor(int major) {
		this.major = major;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public void setJdbcCompliant(boolean jdbcCompliant) {
		this.jdbcCompliant = jdbcCompliant;
	}

	public void setParas(String properties) {
    	if (properties != null && properties.trim().length() > 0) {
    		Properties paras = new Properties();
	    	String[] kvs = properties.split("\\|\\|");
	    	for(String kv : kvs) if (kv.trim().length() > 0) {
	    		String[] ps = kv.split("\\|");
				String key = ps[0].trim();
				if(key.length() > 0 && ps.length > 1) paras.put(key, ps[1].trim());
	    	}
	    	if (this.paras == null) this.paras = paras;
	    	else this.paras.putAll(paras);
    	}
	}

	public void setProperties(Map<Object, Object> paras) {
    	if (this.paras == null) this.paras = new Properties();
    	for (Entry<Object, Object> entry : paras.entrySet()) if (entry.getKey() != null && entry.getValue() != null) this.paras.put(entry.getKey(), entry.getValue());
	}
	
	public void setSource(String source) {
		this.source = source;
	}

    public void setClassloader(ClassLoader classloader) {
		this.classloader = classloader;
	}

	@Override
	public DataSource process(Map<Object, Object> map) {
        try {
        	Object source = map.get("source");
    		if (source == null) source = this.source;
    		Properties newparas = paras == null || paras.size() == 0 ? new Properties() : (Properties) paras.clone(); 
            newparas.putAll(map);
            return source == null ? new SearchDataSource(newparas) : ((Class<DataSource>) (classloader == null ? Class.forName(source.toString()) : classloader.loadClass(source.toString()))).getConstructor(Properties.class).newInstance(newparas);
        } catch (Exception e) {
            log.error("ClassNotFoundException", e);
            throw new PlatformException(SearchError.ERROR_CONNECTION_FAIL, "ClassNotFoundException", e);
        } 
	}

	@Override
	public Connection perform(String user, String password) {
        try {
			if (dataSource == null) synchronized(this) {
				if (dataSource == null) {
					Properties newparas = paras == null || paras.size() == 0 ? new Properties() : (Properties) paras.clone(); 
					dataSource = source == null ? new SearchDataSource(newparas) : ((Class<DataSource>) (classloader == null ? Class.forName(source) : classloader.loadClass(source))).getConstructor(Properties.class).newInstance(newparas);
				}
			}
	        return user == null && password == null ? dataSource.getConnection() : dataSource.getConnection(user, password);
        } catch (Exception e) {
            log.error("ClassNotFoundException", e);
            throw new PlatformException(SearchError.ERROR_CONNECTION_FAIL, "ClassNotFoundException", e);
        }   
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		Properties props = paras == null ? new Properties() : (Properties) paras.clone();
		if (url != null) {
		    String s = url.substring(prefix.length());
		    int i = 0;
		    int n = s.length();
		    StringBuilder nameBuf = new StringBuilder();
		    StringBuilder valueBuf = new StringBuilder();
		    while (i < n) {
		    	nameBuf.setLength(0);
outer:		    while (true) {
					char c = s.charAt(i);
					switch (c) {
						case '=': i++;
							if ((i < n) && ((c = s.charAt(i)) == '=')) {
								i++;
								nameBuf.append(c);
								break;
							}
							break outer;
						case ' ': if (nameBuf.length() == 0) {
							i++;
							break;
						}
						default: nameBuf.append(c);
							i++;
							if (i >= n) break outer;
					}
			    }
			    String name = nameBuf.toString().trim();
			    String value = "";
			    if (i < n) if (s.charAt(i) == ';') i++;
			    else {
			    	char c;
				    while ((c = s.charAt(i)) == ' ') i++;
				    if (i < n) if ((c == '"') || (c == '\'')) {
				    	valueBuf.setLength(0);
				    	while (++i < n) {
				    		char q = s.charAt(i);
				    		if (q == c) {
				    			i++;
				    			if (i < n) {
				    				q = s.charAt(i);
				    				if (q == c) {
				    					valueBuf.append(q);
				    					continue;
				    				}
				    			}
				    			value = valueBuf.toString();
				    			break;
				    		} else valueBuf.append(q);
						}
						if (i >= n) throw new SQLException("Connect string '" + s + "' contains unterminated quoted value '"+ valueBuf.toString() + "'");
						while (i < n && s.charAt(i) == ' ') i++;
						if (i < n) if (s.charAt(i) == ';') i++;
						else throw new SQLException("quoted value ended too soon, at position " + i + " in '" + s + "'");
				    } else {
				    	int semi = s.indexOf(';', i);
				    	if (semi >= 0) {
				    		value = s.substring(i, semi);
						    i = semi + 1;
				    	} else {
						    value = s.substring(i);
						    i = n;
				    	}
				    }
				    props.put(name, value);
			    }
			}
		}
		if (info != null) props.putAll(info);
		DataSource ds = process(props);
		return  ds == null ? null : ds.getConnection();
	}
	
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith(prefix);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
	    List<DriverPropertyInfo> list = new LinkedList<DriverPropertyInfo>();
	    if (info != null) for (Map.Entry<Object, Object> entry : info.entrySet()) list.add(new DriverPropertyInfo((String) entry.getKey(), (String) entry.getValue()));
	    if (paras != null) for (Object p : paras.keySet()) if (info == null || !info.containsKey(p)) list.add(new DriverPropertyInfo(p.toString(), null));
	    return list.toArray(new DriverPropertyInfo[list.size()]);
	}

	@Override
	public int getMajorVersion() {
		return major;
	}

	@Override
	public int getMinorVersion() {
		return minor;
	}

	@Override
	public boolean jdbcCompliant() {
		return jdbcCompliant;
	}
	
	public java.util.logging.Logger getParentLogger(){
		return logger;
	}
	
	public static class SLF4JBridgeHandler extends Handler {
	    protected static final String FQCN = java.util.logging.Logger.class.getName();
	    protected static final String UNKNOWN_LOGGER_NAME = "unknown.jul.logger";
	    protected static final int TRACE_LEVEL_THRESHOLD = Level.FINEST.intValue();
	    protected static final int DEBUG_LEVEL_THRESHOLD = Level.FINE.intValue();
	    protected static final int INFO_LEVEL_THRESHOLD = Level.INFO.intValue();
	    protected static final int WARN_LEVEL_THRESHOLD = Level.WARNING.intValue();
	    protected final Logger log;

	    public SLF4JBridgeHandler() {
	    	this.log = null;
	    }
	    
	    public SLF4JBridgeHandler(Logger log) {
	    	this.log = log;
	    }

	    public void close() {}

	    public void flush() {}

	    protected Logger getSLF4JLogger(LogRecord record) {
	    	if (log == null) {
		        String name = record.getLoggerName();
		        if (name == null) {
		            name = UNKNOWN_LOGGER_NAME;
		        }
		        return LoggerFactory.getLogger(name);
	    	} else return log;
	    }

	    protected void callLocationAwareLogger(LocationAwareLogger lal, LogRecord record) {
	        int julLevelValue = record.getLevel().intValue();
	        int slf4jLevel;
	        if (julLevelValue <= TRACE_LEVEL_THRESHOLD) slf4jLevel = LocationAwareLogger.TRACE_INT;
	        else if (julLevelValue <= DEBUG_LEVEL_THRESHOLD) slf4jLevel = LocationAwareLogger.DEBUG_INT;
	        else if (julLevelValue <= INFO_LEVEL_THRESHOLD) slf4jLevel = LocationAwareLogger.INFO_INT;
	        else if (julLevelValue <= WARN_LEVEL_THRESHOLD) slf4jLevel = LocationAwareLogger.WARN_INT;
	        else slf4jLevel = LocationAwareLogger.ERROR_INT;
	        String i18nMessage = getMessageI18N(record);
	        lal.log(null, FQCN, slf4jLevel, i18nMessage, null, record.getThrown());
	    }

	    protected void callPlainSLF4JLogger(Logger slf4jLogger, LogRecord record) {
	        String i18nMessage = getMessageI18N(record);
	        int julLevelValue = record.getLevel().intValue();
	        if (julLevelValue <= TRACE_LEVEL_THRESHOLD) slf4jLogger.trace(i18nMessage, record.getThrown());
	        else if (julLevelValue <= DEBUG_LEVEL_THRESHOLD) slf4jLogger.debug(i18nMessage, record.getThrown());
	        else if (julLevelValue <= INFO_LEVEL_THRESHOLD) slf4jLogger.info(i18nMessage, record.getThrown());
	        else if (julLevelValue <= WARN_LEVEL_THRESHOLD) slf4jLogger.warn(i18nMessage, record.getThrown());
	        else slf4jLogger.error(i18nMessage, record.getThrown());
	    }
	    
	    protected String getMessageI18N(LogRecord record) {
	        String message = record.getMessage();
	        if (message == null) return null;
	        ResourceBundle bundle = record.getResourceBundle();
	        if (bundle != null) try {
                message = bundle.getString(message);
            } catch (MissingResourceException e) {}
	        Object[] params = record.getParameters();
	        if (params != null && params.length > 0) try {
                message = MessageFormat.format(message, params);
            } catch (IllegalArgumentException e) {
                return message;
            }
	        return message;
	    }

	    public void publish(LogRecord record) {
	        if (record == null) return;
	        Logger slf4jLogger = getSLF4JLogger(record);
	        String message = record.getMessage(); 
	        if (message == null) message = "";
	        if (slf4jLogger instanceof LocationAwareLogger) callLocationAwareLogger((LocationAwareLogger) slf4jLogger, record);
	        else callPlainSLF4JLogger(slf4jLogger, record);
	    }
	}
}

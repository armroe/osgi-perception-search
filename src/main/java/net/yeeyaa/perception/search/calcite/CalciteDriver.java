package net.yeeyaa.perception.search.calcite;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PreDestroy;

import net.yeeyaa.eight.IResource;
import net.yeeyaa.eight.ITriProcessor;

import org.apache.calcite.avatica.ConnectionConfigImpl;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.jdbc.CalcitePrepare.Context;
import org.apache.calcite.jdbc.CalcitePrepare.Query;
import org.apache.calcite.jdbc.Driver;
import org.apache.calcite.jdbc.CalcitePrepare.CalciteSignature;
import org.apache.calcite.linq4j.function.Function0;
import org.apache.calcite.prepare.CalcitePrepareImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CalciteDriver extends Driver {
    protected final Logger log;
    protected final Field field;
    protected IResource<String, CalciteSignature<?>> cache;
    protected ITriProcessor<Query, Context, Type, Query> convertor;
    protected String prefix = "jdbc:psc:";
    protected String[] keys;

	public void setPrefix(String prefix) {
		if (prefix != null) this.prefix = prefix;
	}

	public void setConvertor(ITriProcessor<Query, Context, Type, Query> convertor) {
		this.convertor = convertor;
	}

	public void setCache(IResource<String, CalciteSignature<?>> cache) {
		this.cache = cache;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public CalciteDriver(Logger log) throws SecurityException, NoSuchFieldException {
		this.log = log == null ? LoggerFactory.getLogger(CalciteDriver.class) : log;
    	field = ConnectionConfigImpl.class.getDeclaredField("properties");
        field.setAccessible(true);
    	register();
	}

	public CalciteDriver() throws SecurityException, NoSuchFieldException {
		this.log = LoggerFactory.getLogger(CalciteDriver.class);
    	field = ConnectionConfigImpl.class.getDeclaredField("properties");
        field.setAccessible(true);
    	register();
	}
	
	@PreDestroy
	public void finalize() throws SQLException {
    	DriverManager.deregisterDriver(this);
	}
	
	
	protected String getConnectStringPrefix() {
        return prefix;
    }

    protected Function0<CalcitePrepare> createPrepareFactory() {
        return new Function0<CalcitePrepare>() {
			@Override
			public CalcitePrepare apply() {
	            return new CalcitePrepareImpl(){
	                public <T> CalciteSignature<T> prepareSql(Context context, Query<T> query, Type elementType, long maxRowCount) {
	                	if (convertor != null) query = convertor.operate(query, context, elementType);
	                	if (cache == null) return super.prepareSql(context, query, elementType, maxRowCount);
	                	StringBuilder sb = new StringBuilder(query.sql);
	                    if (keys != null && keys.length > 0) try {
	                        Properties kv = (Properties) field.get(context.config());
	                        for (String key : keys) sb.append('*').append(kv.getProperty(key));
	                    } catch (Exception e) {
	                        log.error("cannot find key.", e);
	                    }
	                    String key = sb.toString();
	                    CalciteSignature signature = cache.find(key);
	                    if (signature == null) {
	                        signature = super.prepareSql(context, query, elementType, maxRowCount);
	                        cache.store(signature, key);
	                    }
	                    return signature;
	                }
	            };
			}
        };
    }
}

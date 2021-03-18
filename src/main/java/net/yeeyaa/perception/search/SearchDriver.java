package net.yeeyaa.perception.search;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PreDestroy;

import net.yeeyaa.eight.IProcessor;
import net.yeeyaa.eight.core.PlatformPool.Local;

import org.apache.calcite.adapter.enumerable.EnumerableInterpretable;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.jdbc.Driver;
import org.apache.calcite.linq4j.function.Function0;
import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.prepare.CalcitePrepareImpl;


public class SearchDriver extends Driver {
	public String prefix = "jdbc:search:";
	protected ClassLoader cl;

	public void setRegister(Boolean register) {
		if (Boolean.TRUE.equals(register)) register();
	}
	
	public void setClassloader(final IProcessor<Object, Class<?>> cl) {
		this.cl = cl == null ? getClass().getClassLoader() : cl instanceof ClassLoader ? (ClassLoader) cl : new ClassLoader(getClass().getClassLoader()) {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				try {
					return getParent().loadClass(name);
				} catch (Exception ex) {
					try {
						Class<?> clazz = cl.process(name);
						if (clazz == null) throw new ClassNotFoundException(name);
						else return clazz;
					} catch (Exception exception) {
						throw new ClassNotFoundException(name);
					}
				}
			}
		};
	}
	
	protected String getConnectStringPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@PreDestroy
	public void finalize() throws SQLException {
		DriverManager.deregisterDriver(this);
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (cl == null) return super.connect(url, info);
		Local<Class<?>, ClassLoader> modelHandler = new Local<Class<?>, ClassLoader>(ModelHandler.class);
		modelHandler.set(cl);
		try {
			return super.connect(url, info);
		} finally {
			modelHandler.remove();
		}
	}

	protected Function0<CalcitePrepare> createPrepareFactory() {
		return new Function0<CalcitePrepare>() {
			@Override
			public CalcitePrepare apply() {
				return new CalcitePrepareImpl() {
					public <T> CalciteSignature<T> prepareSql(Context context, Query<T> query, Type elementType, long maxRowCount) {
						if (cl == null) return super.prepareSql(context, query, elementType, maxRowCount);
						else {
							Thread current = Thread.currentThread();
							ClassLoader parent = current.getContextClassLoader();
							Local<Class<?>, ClassLoader> enumerableInterpretable = new Local<Class<?>, ClassLoader>(EnumerableInterpretable.class);
							enumerableInterpretable.set(cl);
							try {
								current.setContextClassLoader(cl);
								return super.prepareSql(context, query, elementType, maxRowCount);
							} finally {
								enumerableInterpretable.remove();
								current.setContextClassLoader(parent);
							}
						}
					}
				};
			}
		};
	}
}

package net.yeeyaa.perception.search;

import net.yeeyaa.eight.IBiProcessor;
import net.yeeyaa.eight.IProcessor;
import net.yeeyaa.eight.ITriProcessor;
import net.yeeyaa.eight.PlatformException;
import net.yeeyaa.perception.search.SearchFactory.ProxySchema;

import java.lang.reflect.Type;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.materialize.Lattice;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.AggregateFunction;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.FunctionParameter;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.SchemaVersion;
import org.apache.calcite.schema.Schemas;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.TableFunction;
import org.apache.calcite.schema.impl.ReflectiveFunctionBase.ParameterListBuilder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class SearchSchema implements SchemaPlus, IProcessor<Object, Object> {
	protected final Map<String, Object> operand;
	protected final SchemaPlus parentSchema;
	protected final String name;
	protected final Boolean mutable;
	protected final Boolean plus;
	protected final ITriProcessor<SchemaPlus, String, Class<?>, Expression> expression;
	protected final ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Table>> tables;
	protected final ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Schema>> schemas;
	protected final ITriProcessor<SchemaPlus, String, Map<String, Object>, Multimap<String, Function>> funcs;
	protected volatile Map<String, Table> tableMap;
	protected volatile Map<String, Schema> schemaMap;
	protected volatile Multimap<String, Function> funcMap;
	protected volatile boolean cache;
	
	public enum Types {
        Byte(Byte.class),
        Short(Short.class),
        Integer(Integer.class),
        Long(Long.class),
        Float(Float.class),
        Double(Double.class),
        Decimal(BigDecimal.class),
        String(String.class),
        Binary(byte[].class),
        Boolean(Boolean.class),
        Timestamp(Timestamp.class),
        Date(Date.class),
        Time(Time.class),
        Any(Object.class);

        protected final Class<?> value;

        private Types(Class<?> value) {
            this.value = value;
        }
        
        public Class<?> getValue() {
        	return value;
        }
    }
  
    public static Class<?> getType(String value, ClassLoader cl) {
    	try {
    		return Types.valueOf(value).getValue();
    	} catch (Exception e) {
    		try {
	    		return cl.loadClass(value);
    		} catch (Exception ex) {
    			throw new PlatformException(SearchError.SCHEMA_ERROR, ex);
    		}
    	}
	}
	
    public static Class<?> getType(String value) {
    	return getType(value, Types.class.getClassLoader());
    }
    
    public static class SFunction implements ScalarFunction {
		protected final List<FunctionParameter> parameter;
		protected Class<?> type;
		
		public SFunction(List<Object> paras) {
			ParameterListBuilder builder = new ParameterListBuilder();
			for (Object t : paras) if (t instanceof Object[]) {
				Object[] ps = (Object[]) t;
				builder.add(ps[1] instanceof Class<?> ? (Class<?>) ps[1] : getType(ps[1].toString()), ps[0].toString(), ps.length > 2);
			} else if (type == null) type = t instanceof Class ? (Class<?>) t : getType(t.toString());
			parameter = builder.build();
		}
		
		@Override
		public List<FunctionParameter> getParameters() {
			return parameter;
		}
	
		@Override
		public RelDataType getReturnType(RelDataTypeFactory typeFactory) {
			return typeFactory.createJavaType(type);
		}
	}
	  
	public static class AFunction implements AggregateFunction {
		protected final List<FunctionParameter> parameter;
		protected Class<?> type;

		public AFunction(List<Object> paras) {
			ParameterListBuilder builder = new ParameterListBuilder();
			for (Object t : paras) if (t instanceof Object[]) {
				Object[] ps = (Object[]) t;
				builder.add(ps[1] instanceof Class<?> ? (Class<?>) ps[1] : getType(ps[1].toString()), ps[0].toString(), ps.length > 2);
			} else if (type == null) type = t instanceof Class ? (Class<?>) t : getType(t.toString());
			parameter = builder.build();
		}

		@Override
		public List<FunctionParameter> getParameters() {
			return parameter;
		}

		@Override
		public RelDataType getReturnType(RelDataTypeFactory typeFactory) {
			return typeFactory.createJavaType(type);
		}  
	}
	
	public static class TFunction implements TableFunction {
		protected final List<FunctionParameter> parameter;
		protected IBiProcessor<RelDataTypeFactory, List<Object>, Object> processor;
		
		public TFunction(List<Object> paras) {
			ParameterListBuilder builder = new ParameterListBuilder();
			for (Object t : paras) if (t instanceof Object[]) {
				Object[] ps = (Object[]) t;
				builder.add(ps[1] instanceof Class<?> ? (Class<?>) ps[1] : getType(ps[1].toString()), ps[0].toString(), ps.length > 2);
			} else if (processor == null && t instanceof IBiProcessor) processor = (IBiProcessor<RelDataTypeFactory, List<Object>, Object>) t;
			parameter = builder.build();
		}

		@Override
		public List<FunctionParameter> getParameters() {
			return parameter;
		}

		@Override
		public RelDataType getRowType(RelDataTypeFactory typeFactory,  List<Object> arguments) {
			return (RelDataType) processor.perform(typeFactory, arguments);
		}

		@Override
		public Type getElementType(List<Object> arguments) {
			return (Type) processor.perform(null, arguments);
		}
	}
	
	protected SearchSchema(Map<String, Table> tableMap, Multimap<String, Function> funcMap, Map<String, Schema> schemaMap,
			ITriProcessor<SchemaPlus, String, Class<?>, Expression> expression,
			ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Table>> tables,
			ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Schema>> schemas,
			ITriProcessor<SchemaPlus, String, Map<String, Object>, Multimap<String, Function>> funcs,
			SchemaPlus parentSchema, String name, Map<String, Object> operand, Boolean mutable, Boolean plus) {
		this.parentSchema = parentSchema;
		this.name = name;
		this.tables = tables;
		this.schemas = schemas;
		this.mutable = mutable;
		this.plus = plus;
		this.funcs = funcs;
		this.tableMap = tableMap;
		this.operand = operand;
		this.funcMap = funcMap;
		this.schemaMap = schemaMap;
		this.expression = expression;
	}

	public boolean isMutable() {
		return mutable;
	}

	public Schema snapshot(SchemaVersion version) {
		return new SearchSchema(tableMap, funcMap, schemaMap, expression, tables, schemas, funcs, parentSchema, name, operand, mutable, plus);
	}

	public Expression getExpression(SchemaPlus parentSchema, String name) {
		return expression == null ? Schemas.subSchemaExpression(parentSchema, name, SearchSchema.class) : expression.operate(parentSchema, name, SearchSchema.class);
	}

	protected Multimap<String, Function> getFunctions() {
		if (funcMap == null && funcMap != null) synchronized(this) { if (funcMap == null && funcMap != null) funcMap = funcs.operate(parentSchema, name, operand); }
		return funcMap;
	}

	public final Collection<Function> getFunctions(String name) {
		Multimap<String, Function> funcMap = getFunctions();
		return funcMap == null ? null : funcMap.get(name);
	}

	public final Set<String> getFunctionNames() {
		Multimap<String, Function> funcMap = getFunctions();
		return funcMap == null ? Collections.EMPTY_SET : funcMap.keySet();
	}

	public Table getTable(String name) {
		Map<String, Table> tableMap = getTableMap();
		return tableMap == null ? null : tableMap.get(name);
	}

	protected Map<String, Table> getTableMap() {
		if (tableMap == null && tables != null) synchronized(this) { if (tableMap == null && tables != null) tableMap = tables.operate(parentSchema, name, operand); }
		return tableMap;
	}

	public Set<String> getTableNames() {
		Map<String, Table> tableMap = getTableMap();
		return tableMap == null ? Collections.EMPTY_SET : tableMap.keySet();
	}

	protected Map<String, Schema> getSchemaMap() {
		if (schemaMap == null && schemas != null) synchronized(this) { if (schemaMap == null && schemas != null) schemaMap = schemas.operate(parentSchema, name, operand); }
		return schemaMap;
	}
	
	public SchemaPlus getSubSchema(String name) {
		Map<String, Schema> schemaMap = getSchemaMap();
		Schema schema = schemaMap == null ? null : schemaMap.get(name);
		return schema == null ? null : schema instanceof SchemaPlus ? (SchemaPlus) schema : new ProxySchema(schema);
	}

	public Set<String> getSubSchemaNames() {
		Map<String, Schema> schemaMap = getSchemaMap();
		return schemaMap == null ? Collections.EMPTY_SET : schemaMap.keySet();
	}

	@Override
	public SchemaPlus getParentSchema() {
		return parentSchema;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SchemaPlus add(String name, Schema schema) {
		if (!plus) throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		Map<String, Schema> schemaMap = getSchemaMap();
		if (schemaMap == null) return null;
		else {
			SchemaPlus plus = new ProxySchema(schema);
			schemaMap.put(name, plus);
			return plus;
		}
	}

	@Override
	public void add(String name, Table table) {
		if (!plus) throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		Map<String, Table> tableMap = getTableMap();
		if (tableMap != null) tableMap.put(name, table);
	}

	@Override
	public void add(String name, Function function) {
		if (!plus) throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		Multimap<String, Function> funcMap = getFunctions();
		if (funcMap != null) funcMap.put(name, function);
	}

	@Override
	public void add(String name, Lattice lattice) {
		if (!plus) throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
	}

	@Override
	public <T> T unwrap(Class<T> clazz) {
		if (!plus) throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		return (T) this;
	}

	@Override
	public void setPath(ImmutableList<ImmutableList<String>> path) {
		if (!plus) throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
	}

	@Override
	public void setCacheEnabled(boolean cache) {
		this.cache = cache;
	}

	@Override
	public boolean isCacheEnabled() {
		return cache;
	}
	
	public static class Factory implements SchemaFactory, IBiProcessor<Boolean, List<Object>, Function>,  ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema> {
		protected final ClassLoader classloader;
		protected final IBiProcessor<Object, Object, Object> holder;
		protected final String classname;
		
		public Factory() {
			classloader = getClass().getClassLoader();
			String[] name = getClass().getName().split("\\.");
			classname = name[name.length - 1];
			holder = classloader instanceof IBiProcessor ? (IBiProcessor<Object, Object, Object>) classloader : null;			
		}

		public Schema create(SchemaPlus parentSchema, String name,	Map<String, Object> operand) {
			final String i = (String) operand.get("instance");
			if (i != null) {
				ITriProcessor<SchemaPlus, String, Map<String, Object>, Void> instance = (ITriProcessor<SchemaPlus, String, Map<String, Object>, Void>) holder.perform(classname, i);
				if (instance != null) instance.operate(parentSchema, name, operand);		
			}
			final String ts = (String) operand.get("tables");
			ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Table>> tables = ts == null ? null : (ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Table>>) holder.perform(classname, ts);
			final String ss = (String) operand.get("schemas");
			ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Schema>> schemas = ss == null ? null : (ITriProcessor<SchemaPlus, String, Map<String, Object>, Map<String, Schema>>) holder.perform(classname, ss);
			final String fs = (String) operand.get("funcs");
			ITriProcessor<SchemaPlus, String, Map<String, Object>, Multimap<String, Function>> funcs = fs == null ? null : (ITriProcessor<SchemaPlus, String, Map<String, Object>, Multimap<String, Function>>) holder.perform(classname, fs);
			final String ex = (String) operand.get("expression");
			ITriProcessor<SchemaPlus, String, Class<?>, Expression> expression = ex == null ? null : (ITriProcessor<SchemaPlus, String, Class<?>, Expression>) holder.perform(classname, ex);
			final Boolean mutable = "true".equals(operand.get("mutable"));
			final Boolean plus = "true".equals(operand.get("plus"));
			return new SearchSchema(null, null, null, expression, tables, schemas, funcs, parentSchema, name, operand, mutable, plus);
		}
		
		@Override
		public Schema operate(SchemaPlus parentSchema, Entry<Object, Object> info, Map<String, Object> operand) {
			return create(parentSchema, info.getKey().toString(), operand);
		}

		@Override
		public Function perform(Boolean flag, List<Object> paras) {
			if (classloader != null) {
				Iterator<Object> itr = paras.iterator();
				String type = null;
				while (itr.hasNext()) {
					Object t = itr.next();
					 if (t instanceof Object[]) {
							Object[] ps = (Object[]) t;
							if (ps[1] instanceof String) ps[1] = getType(ps[1].toString(), classloader);
					} else if (t instanceof String) {
						itr.remove();
						if (type == null) type = t.toString();
					}
				}
				if (type != null) paras.add(getType(type, classloader));
			}
			if (flag == null) return new SFunction(paras);
			else if (flag) return new AFunction(paras);
			else  return new TFunction(paras);
		}
	}

	@Override
	public Object process(Object parameter) {
		if (parameter == null || operand == null) return null;
		else return operand.get(parameter);
	}
}

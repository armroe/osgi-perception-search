package net.yeeyaa.perception.search;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.yeeyaa.eight.IBiProcessor;
import net.yeeyaa.eight.IExtendible;
import net.yeeyaa.eight.IInputResource;
import net.yeeyaa.eight.IListable;
import net.yeeyaa.eight.IListableResource;
import net.yeeyaa.eight.IOutputResource;
import net.yeeyaa.eight.IProcessor;
import net.yeeyaa.eight.ITransaction;
import net.yeeyaa.eight.ITriProcessor;
import net.yeeyaa.eight.IUniversal;
import net.yeeyaa.eight.PlatformException;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.materialize.Lattice;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.SchemaVersion;
import org.apache.calcite.schema.Table;

import com.google.common.collect.ImmutableList;


public class SearchFactory implements SchemaFactory, ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema> {
	protected final ClassLoader classloader;
	protected final IBiProcessor<Object, Object, Object> holder;
	protected final String classname;
	
	public SearchFactory() {
		classloader = getClass().getClassLoader();
		String[] name = getClass().getName().split("\\.");
		classname = name[name.length - 1];
		holder = (IBiProcessor<Object, Object, Object>) classloader;
	}

	@Override
	public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
		return new SchemaProxy(parentSchema, name, operand);
	}
	
	@Override
	public Schema operate(SchemaPlus parentSchema, Entry<Object, Object> name, Map<String, Object> operand) {
		return new SchemaProxy(parentSchema, name.getKey().toString(), operand);
	}
	
	protected static class ProxySchema<K, V, U extends IListableResource<K, V>, T, R> implements SchemaPlus, IUniversal<K, V, U, T, R> {
		protected Schema schema;
		
		public ProxySchema(Schema schema) {
			this.schema = schema;
		}

		@Override
		public Table getTable(String name) {
			return schema.getTable(name);
		}

		@Override
		public Set<String> getTableNames() {
			return schema.getTableNames();
		}

		@Override
		public Collection<Function> getFunctions(String name) {
			return schema.getFunctions(name);
		}

		@Override
		public Set<String> getFunctionNames() {
			return schema.getFunctionNames();
		}

		@Override
		public Set<String> getSubSchemaNames() {
			return schema.getSubSchemaNames();
		}

		@Override
		public Expression getExpression(SchemaPlus parentSchema,String name) {
			return schema.getExpression(parentSchema, name);
		}

		@Override
		public Schema snapshot(SchemaVersion version) {
			return new ProxySchema<K, V, U, T, R>(schema.snapshot(version));
		}

		@Override
		public SchemaPlus getSubSchema(String name) {
			Schema s = schema.getSubSchema(name);
			return s instanceof SchemaPlus ? (SchemaPlus) s : new ProxySchema<K, V, U, T, R>(s);
		}

		@Override
		public boolean isMutable() {
			return schema.isMutable();
		}

		@Override
		public SchemaPlus getParentSchema() {
			if (schema instanceof SchemaPlus) return ((SchemaPlus)schema).getParentSchema();
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public String getName() {
			if (schema instanceof SchemaPlus) return ((SchemaPlus)schema).getName();
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public SchemaPlus add(String name, Schema schema) {
			if (schema instanceof SchemaPlus) return ((SchemaPlus)schema).add(name, schema);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public void add(String name, Table table) {
			if (schema instanceof SchemaPlus) ((SchemaPlus)schema).add(name, table);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);		}

		@Override
		public void add(String name, Function function) {
			if (schema instanceof SchemaPlus) ((SchemaPlus)schema).add(name, function);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public void add(String name, Lattice lattice) {
			if (schema instanceof SchemaPlus) ((SchemaPlus)schema).add(name, lattice);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <T> T unwrap(Class<T> clazz) {
			if (schema instanceof SchemaPlus) return ((SchemaPlus)schema).unwrap(clazz);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public void setPath(ImmutableList<ImmutableList<String>> path) {
			if (schema instanceof SchemaPlus) ((SchemaPlus)schema).setPath(path);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public void setCacheEnabled(boolean cache) {
			if (schema instanceof SchemaPlus) ((SchemaPlus)schema).setCacheEnabled(cache);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public boolean isCacheEnabled() {
			if (schema instanceof SchemaPlus) return ((SchemaPlus)schema).isCacheEnabled();
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public R process(T object) {
			if (schema instanceof IProcessor) return ((IProcessor<T, R>)schema).process(object);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public R perform(K first, V second) {
			if (schema instanceof IBiProcessor) return ((IBiProcessor<K, V, R>)schema).perform(first, second);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public R operate(T first, K second, V third) {
			if (schema instanceof ITriProcessor) return ((ITriProcessor<T, K, V, R>)schema).operate(first, second, third);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public V find(K... paras) {
			if (schema instanceof IInputResource) return ((IInputResource<K, V>)schema).find(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <P> P store(V value, K... paras) {
			if (schema instanceof IOutputResource) return ((IOutputResource<K, V>)schema).store(value, paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <P> P discard(K... paras) {
			if (schema instanceof IOutputResource) return ((IOutputResource<K, V>)schema).discard(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <P> P empty(K... paras) {
			if (schema instanceof IOutputResource) return ((IOutputResource<K, V>)schema).empty(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public Collection<K[]> keys(K... paras) {
			if (schema instanceof IListable) return ((IListable<K, V>)schema).keys(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public Map<K[], V> all(K... paras) {
			if (schema instanceof IListable) return ((IListable<K, V>)schema).all(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public R execute(IProcessor<U, R> processor) {
			if (schema instanceof ITransaction) return ((ITransaction<K, V, U, R>)schema).execute(processor);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <M, L, N> IProcessor<L, N> extend(M method) {
			if (schema instanceof IExtendible) return ((IExtendible)schema).extend(method);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <M> Collection<M> methods() {
			if (schema instanceof IExtendible) return ((IExtendible)schema).methods();
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <O> O realObject() {
			if (schema instanceof IUniversal) return ((IUniversal<K, V, U, T, R>)schema).realObject();
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}
	};
	
	protected class SchemaProxy<K, V, U extends IListableResource<K, V>, T, R> implements SchemaPlus, IUniversal<K, V, U, T, R> {
		protected final SchemaPlus parentSchema;
		protected final Entry<Object, Object> info;
		protected Map<String, Object> operand;
		
		public SchemaProxy(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
			this.parentSchema = parentSchema;
			this.info = new SimpleEntry<Object, Object>(name, UUID.randomUUID());
			this.operand = operand;
		}

		@Override
		public Table getTable(String name) {
			return ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).getTable(name) ;
		}

		@Override
		public Set<String> getTableNames() {
			return ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).getTableNames();
		}

		@Override
		public Collection<Function> getFunctions(String name) {
			return ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).getFunctions(name);
		}

		@Override
		public Set<String> getFunctionNames() {
			return ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).getFunctionNames();
		}

		@Override
		public SchemaPlus getSubSchema(String name) {
			Schema schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).getSubSchema(name);
			return schema instanceof SchemaPlus ? (SchemaPlus) schema : new ProxySchema(schema);
		}

		@Override
		public Set<String> getSubSchemaNames() {
			return ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).getSubSchemaNames();
		}

		@Override
		public Expression getExpression(SchemaPlus parentSchema, String name) {
			return ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).getExpression(parentSchema, name);
		}

		@Override
		public boolean isMutable() {
			return ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).isMutable();
		}

		@Override
		public Schema snapshot(final SchemaVersion version) {
			SchemaProxy<K, V, U, T, R> schema =  new SchemaProxy<K, V, U, T, R>(parentSchema, info.getKey().toString(), operand);
			SchemaVersion id = new SchemaVersion() {
				protected final UUID id = UUID.randomUUID();
				
				@Override
				public boolean isBefore(SchemaVersion other) {
					return version.isBefore(other);
				}
				
				@Override
				public int hashCode() {
					return id.hashCode();
				}
				
				@Override
				public boolean equals(Object obj) {
					if (getClass().isInstance(obj)) return id.equals(getClass().cast(obj).id);
					return false;
				}
				
				@Override
				public String toString() {
					return id.toString();
				}				
			};
			schema.info.setValue(id);
			((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand).snapshot(id);
			return schema;
		}

		@Override
		public SchemaPlus getParentSchema() {
			return parentSchema;
		}

		@Override
		public String getName() {
			return info.getKey().toString();
		}

		@Override
		public SchemaPlus add(String name, Schema schema) {
			return ((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).add(name, schema);
		}

		@Override
		public void add(String name, Table table) {
			((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).add(name, table);
		}

		@Override
		public void add(String name, Function function) {
			((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).add(name, function);
		}

		@Override
		public void add(String name, Lattice lattice) {
			((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).add(name, lattice);
		}

		@Override
		public <T> T unwrap(Class<T> clazz) {
			return ((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).unwrap(clazz);
		}

		@Override
		public void setPath(ImmutableList<ImmutableList<String>> path) {
			((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).setPath(path);
		}

		@Override
		public void setCacheEnabled(boolean cache) {
			((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).setCacheEnabled(cache);
		}

		@Override
		public boolean isCacheEnabled() {
			return ((SchemaPlus)((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand)).isCacheEnabled();
		}

		@Override		
		public R process(T object) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IProcessor) return ((IProcessor<T, R>)schema).process(object);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public R perform(K first, V second) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IBiProcessor) return ((IBiProcessor<K, V, R>)schema).perform(first, second);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public R operate(T first, K second, V third) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof ITriProcessor) return ((ITriProcessor<T, K, V, R>)schema).operate(first, second, third);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public V find(K... paras) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IInputResource) return ((IInputResource<K, V>)schema).find(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <P> P store(V value, K... paras) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IOutputResource) return ((IOutputResource<K, V>)schema).store(value, paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <P> P discard(K... paras) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IOutputResource) return ((IOutputResource<K, V>)schema).discard(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <P> P empty(K... paras) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IOutputResource) return ((IOutputResource<K, V>)schema).empty(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public Collection<K[]> keys(K... paras) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IListable) return ((IListable<K, V>)schema).keys(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public Map<K[], V> all(K... paras) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IListable) return ((IListable<K, V>)schema).all(paras);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public R execute(IProcessor<U, R> processor) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof ITransaction) return ((ITransaction<K, V, U, R>)schema).execute(processor);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <M, L, N> IProcessor<L, N> extend(M method) {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IExtendible) return ((IExtendible)schema).extend(method);
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <M> Collection<M> methods() {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IExtendible) return ((IExtendible)schema).methods();
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}

		@Override
		public <O> O realObject() {
			Object schema = ((ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>) holder.perform(classname, classname)).operate(parentSchema, info, operand);
			if (schema instanceof IUniversal) return ((IUniversal<K, V, U, T, R>)schema).realObject();
			else throw new PlatformException(SearchError.METHOD_NOT_SUPPORT);
		}
	}
	
	public static class Proxy implements ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema> {
		protected Object key;
		protected IProcessor<Object, ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>> holder;
		
		public void setKey(Object key) {
			this.key = key;
		}

		public void setHolder(IProcessor<Object, ITriProcessor<SchemaPlus, Entry<Object, Object>, Map<String, Object>, Schema>> holder) {
			this.holder = holder;
		}

		@Override
		public Schema operate(SchemaPlus parentSchema, Entry<Object, Object> info, Map<String, Object> operand) {
			return holder.process(key == null ? info.getKey() : operand.get(key)).operate(parentSchema, info, operand);
		}
	}
}

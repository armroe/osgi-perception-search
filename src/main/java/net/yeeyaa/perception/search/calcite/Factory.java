package net.yeeyaa.perception.search.calcite;

import java.lang.ref.WeakReference;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.yeeyaa.eight.IProcessor;
import net.yeeyaa.eight.ITriProcessor;
import net.yeeyaa.eight.PlatformException;
import net.yeeyaa.perception.search.SearchError;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.materialize.Lattice;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.SchemaVersion;
import org.apache.calcite.schema.Table;

import com.google.common.collect.ImmutableList;


public class Factory implements SchemaFactory {
	protected static volatile WeakReference<ITriProcessor<SchemaPlus, Entry<String, Object>, Map<String, Object>, Schema>> holder;
	
	public void setHolder(ITriProcessor<SchemaPlus, Entry<String, Object>, Map<String, Object>, Schema> holder) {
		Factory.holder = new WeakReference<ITriProcessor<SchemaPlus, Entry<String, Object>, Map<String, Object>, Schema>>(holder);
	}

	@Override
	public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
		return new SchemaProxy(parentSchema, name, operand);
	}
	
	protected class ProxySchema implements SchemaPlus {
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
			return schema.snapshot(version);
		}

		@Override
		public SchemaPlus getSubSchema(String name) {
			Schema s = schema.getSubSchema(name);
			return s instanceof SchemaPlus ? (SchemaPlus) s : new ProxySchema(s);
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
	};
	
	protected class SchemaProxy implements SchemaPlus {
		protected final SchemaPlus parentSchema;
		protected final Entry<String, Object> info;
		protected Map<String, Object> operand;
		
		public SchemaProxy(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
			this.parentSchema = parentSchema;
			this.info = new SimpleEntry<String, Object>(name, UUID.randomUUID());
			this.operand = operand;
		}

		@Override
		public Table getTable(String name) {
			return holder.get().operate(parentSchema, info, operand).getTable(name) ;
		}

		@Override
		public Set<String> getTableNames() {
			return holder.get().operate(parentSchema, info, operand).getTableNames();
		}

		@Override
		public Collection<Function> getFunctions(String name) {
			return holder.get().operate(parentSchema, info, operand).getFunctions(name);
		}

		@Override
		public Set<String> getFunctionNames() {
			return holder.get().operate(parentSchema, info, operand).getFunctionNames();
		}

		@Override
		public SchemaPlus getSubSchema(String name) {
			Schema schema = holder.get().operate(parentSchema, info, operand).getSubSchema(name);
			return schema instanceof SchemaPlus ? (SchemaPlus) schema : new ProxySchema(schema);
		}

		@Override
		public Set<String> getSubSchemaNames() {
			return holder.get().operate(parentSchema, info, operand).getSubSchemaNames();
		}

		@Override
		public Expression getExpression(SchemaPlus parentSchema, String name) {
			return holder.get().operate(parentSchema, info, operand).getExpression(parentSchema, name);
		}

		@Override
		public boolean isMutable() {
			return holder.get().operate(parentSchema, info, operand).isMutable();
		}

		@Override
		public Schema snapshot(SchemaVersion version) {
			return holder.get().operate(parentSchema, info, operand).snapshot(version);
		}

		@Override
		public SchemaPlus getParentSchema() {
			return ((SchemaPlus)holder.get().operate(parentSchema, info, operand)).getParentSchema();
		}

		@Override
		public String getName() {
			return ((SchemaPlus)holder.get().operate(parentSchema, info, operand)).getName();
		}

		@Override
		public SchemaPlus add(String name, Schema schema) {
			return ((SchemaPlus)holder.get().operate(parentSchema, info, operand)).add(name, schema);
		}

		@Override
		public void add(String name, Table table) {
			((SchemaPlus)holder.get().operate(parentSchema, info, operand)).add(name, table);
		}

		@Override
		public void add(String name, Function function) {
			((SchemaPlus)holder.get().operate(parentSchema, info, operand)).add(name, function);
		}

		@Override
		public void add(String name, Lattice lattice) {
			((SchemaPlus)holder.get().operate(parentSchema, info, operand)).add(name, lattice);
		}

		@Override
		public <T> T unwrap(Class<T> clazz) {
			return ((SchemaPlus)holder.get().operate(parentSchema, info, operand)).unwrap(clazz);
		}

		@Override
		public void setPath(ImmutableList<ImmutableList<String>> path) {
			((SchemaPlus)holder.get().operate(parentSchema, info, operand)).setPath(path);
		}

		@Override
		public void setCacheEnabled(boolean cache) {
			((SchemaPlus)holder.get().operate(parentSchema, info, operand)).setCacheEnabled(cache);
		}

		@Override
		public boolean isCacheEnabled() {
			return ((SchemaPlus)holder.get().operate(parentSchema, info, operand)).isCacheEnabled();
		}
	}
	
	public static class Proxy implements ITriProcessor<SchemaPlus, Entry<String, Object>, Map<String, Object>, Schema> {
		protected Object key;
		protected IProcessor<Object, ITriProcessor<SchemaPlus, Entry<String, Object>, Map<String, Object>, Schema>> holder;
		
		public void setKey(Object key) {
			this.key = key;
		}

		public void setHolder(IProcessor<Object, ITriProcessor<SchemaPlus, Entry<String, Object>, Map<String, Object>, Schema>> holder) {
			this.holder = holder;
		}

		@Override
		public Schema operate(SchemaPlus parentSchema, Entry<String, Object> info, Map<String, Object> operand) {
			return holder.process(key == null ? info.getKey() : operand.get(key)).operate(parentSchema, info, operand);
		}
	}
}

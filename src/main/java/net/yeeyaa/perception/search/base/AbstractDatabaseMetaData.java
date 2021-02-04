package net.yeeyaa.perception.search.base;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;


public abstract class AbstractDatabaseMetaData implements DatabaseMetaData {
    public ResultSet getTableTypes() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getCatalogs() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getSchemas() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getClientInfoProperties() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getURL() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getUserName() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getDatabaseProductName() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getDatabaseProductVersion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getDriverName() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getDriverVersion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getDriverMajorVersion() {
        return 0;
    }

    public int getDriverMinorVersion() {
        return 0;
    }

    public boolean usesLocalFiles() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getSchemaTerm() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getProcedureTerm() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getCatalogTerm() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isCatalogAtStart() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getCatalogSeparator() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getTypeInfo() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getResultSetHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getDatabaseMajorVersion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getDatabaseMinorVersion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getJDBCMajorVersion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getJDBCMinorVersion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isReadOnly() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean nullsAreSortedHigh() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean nullsAreSortedLow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean nullsAreSortedAtStart() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean nullsAreSortedAtEnd() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean allProceduresAreCallable() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean allTablesAreSelectable() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean usesLocalFilePerTable() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean storesUpperCaseIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean storesLowerCaseIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean storesMixedCaseIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getIdentifierQuoteString() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getSQLKeywords() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getNumericFunctions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getStringFunctions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getSystemFunctions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getTimeDateFunctions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getSearchStringEscape() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public String getExtraNameCharacters() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsColumnAliasing() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean nullPlusNonNullIsNull() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsConvert() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsTableCorrelationNames() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsExpressionsInOrderBy() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsOrderByUnrelated() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsGroupBy() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsGroupByUnrelated() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsGroupByBeyondSelect() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsLikeEscapeClause() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsMultipleResultSets() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsMultipleTransactions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsNonNullableColumns() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsMinimumSQLGrammar() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsCoreSQLGrammar() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsExtendedSQLGrammar() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsANSI92FullSQL() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsOuterJoins() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsFullOuterJoins() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsLimitedOuterJoins() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSchemasInDataManipulation() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsPositionedDelete() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsPositionedUpdate() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSelectForUpdate() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsStoredProcedures() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSubqueriesInComparisons() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSubqueriesInExists() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSubqueriesInIns() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsCorrelatedSubqueries() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsUnion() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsUnionAll() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxBinaryLiteralLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxCharLiteralLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxColumnNameLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxColumnsInGroupBy() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxColumnsInIndex() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxColumnsInOrderBy() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxColumnsInSelect() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxColumnsInTable() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxConnections() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxCursorNameLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxIndexLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxSchemaNameLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxProcedureNameLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxCatalogNameLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxRowSize() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxStatementLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxStatements() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxTableNameLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxTablesInSelect() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getMaxUserNameLength() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public int getDefaultTransactionIsolation() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsTransactions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsSavepoints() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsNamedParameters() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsMultipleOpenResults() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsGetGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean locatorsUpdateCopy() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsStatementPooling() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public RowIdLifetime getRowIdLifetime() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsResultSetType(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean ownDeletesAreVisible(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean ownInsertsAreVisible(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean othersDeletesAreVisible(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean othersInsertsAreVisible(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean updatesAreDetected(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean deletesAreDetected(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean insertsAreDetected(int type) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean supportsBatchUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean generatedKeyAlwaysReturned() throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }
}

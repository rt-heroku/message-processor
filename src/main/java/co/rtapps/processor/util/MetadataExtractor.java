package co.rtapps.processor.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetadataExtractor {
    private final DatabaseMetaData databaseMetaData;

    private JdbcTemplate jdbcTemplate;
    
    public MetadataExtractor(Connection connection) throws SQLException {
        this.databaseMetaData = connection.getMetaData();
    }

    public MetadataExtractor(JdbcTemplate jdbcTemplate) throws SQLException {
    	this.jdbcTemplate = jdbcTemplate;
    	this.databaseMetaData = this.jdbcTemplate.getDataSource().getConnection().getMetaData();
    }

    public void extractTableInfo(String schema, String table) throws SQLException {
        ResultSet resultSet = databaseMetaData.getTables(null, schema, table, new String[] { "TABLE" });
        while (resultSet.next()) {
            // Print the names of existing tables
            log.info(resultSet.getString("TABLE_NAME"));
            log.info(resultSet.getString("REMARKS"));
        }
    }

    public void extractSystemTables() throws SQLException {
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] { "SYSTEM TABLE" });
        while (resultSet.next()) {
            // Print the names of system tables
            log.info(resultSet.getString("TABLE_NAME"));
        }
    }

    public void extractViews() throws SQLException {
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] { "VIEW" });
        while (resultSet.next()) {
            // Print the names of existing views
            log.info(resultSet.getString("TABLE_NAME"));
        }
    }

    public void extractColumnInfo(String schema, String tableName) throws SQLException {
    	ResultSet columns = databaseMetaData.getColumns(null, schema, tableName, null);
    	
    	while (columns.next()) {
    		String columnName = columns.getString("COLUMN_NAME");
    		String columnSize = columns.getString("COLUMN_SIZE");
    		String datatype = columns.getString("DATA_TYPE");
    		String isNullable = columns.getString("IS_NULLABLE");
    		String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
    		log.info(String.format("ColumnName: %s, columnSize: %s, datatype: %s, isColumnNullable: %s, isAutoIncrementEnabled: %s", columnName, columnSize, datatype, isNullable, isAutoIncrement));
    	}
    }

    public void extractPrimaryKeys(String schema, String tableName) throws SQLException {
        ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, schema, tableName);
        while (primaryKeys.next()) {
            String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
            String primaryKeyName = primaryKeys.getString("PK_NAME");
            log.info(String.format("columnName:%s, pkName:%s", primaryKeyColumnName, primaryKeyName));
        }
    }

    public void fun() throws SQLException {

    }

    public void extractForeignKeys(String schema, String tableName) throws SQLException {
        ResultSet foreignKeys = databaseMetaData.getImportedKeys(null, schema, tableName);
        while (foreignKeys.next()) {
            String pkTableName = foreignKeys.getString("PKTABLE_NAME");
            String fkTableName = foreignKeys.getString("FKTABLE_NAME");
            String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
            String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
            log.info(String.format("pkTableName:%s, fkTableName:%s, pkColumnName:%s, fkColumnName:%s", pkTableName, fkTableName, pkColumnName, fkColumnName));
        }
    }

    public void extractDatabaseInfo() throws SQLException {
        String productName = databaseMetaData.getDatabaseProductName();
        String productVersion = databaseMetaData.getDatabaseProductVersion();

        String driverName = databaseMetaData.getDriverName();
        String driverVersion = databaseMetaData.getDriverVersion();

        log.info(String.format("Product name:%s, Product version:%s", productName, productVersion));
        log.info(String.format("Driver name:%s, Driver Version:%s", driverName, driverVersion));
    }

    public void extractUserName() throws SQLException {
        String userName = databaseMetaData.getUserName();
        log.info(userName);
        ResultSet schemas = databaseMetaData.getSchemas();
        while (schemas.next()) {
            String table_schem = schemas.getString("TABLE_SCHEM");
            String table_catalog = schemas.getString("TABLE_CATALOG");
            log.info(String.format("Table_schema:%s, Table_catalog:%s", table_schem, table_catalog));
        }
    }

    public void extractSupportedFeatures() throws SQLException {
        log.info("Supports scrollable & Updatable Result Set: " + databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));
        log.info("Supports Full Outer Joins: " + databaseMetaData.supportsFullOuterJoins());
        log.info("Supports Stored Procedures: " + databaseMetaData.supportsStoredProcedures());
        log.info("Supports Subqueries in 'EXISTS': " + databaseMetaData.supportsSubqueriesInExists());
        log.info("Supports Transactions: " + databaseMetaData.supportsTransactions());
        log.info("Supports Core SQL Grammar: " + databaseMetaData.supportsCoreSQLGrammar());
        log.info("Supports Batch Updates: " + databaseMetaData.supportsBatchUpdates());
        log.info("Supports Column Aliasing: " + databaseMetaData.supportsColumnAliasing());
        log.info("Supports Savepoints: " + databaseMetaData.supportsSavepoints());
        log.info("Supports Union All: " + databaseMetaData.supportsUnionAll());
        log.info("Supports Union: " + databaseMetaData.supportsUnion());
    }
}
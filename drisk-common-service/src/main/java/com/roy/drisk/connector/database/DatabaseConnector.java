package com.roy.drisk.connector.database;

import com.roy.drisk.connector.config.ConnectorProperties;
import com.roy.drisk.connector.service.ClosedStatusAware;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * @author roy
 * @date 2021/10/27
 * @desc 数据库连接器。用于创建及维护数据库连接池，及生成MyBatis的SqlSession
 */
public class DatabaseConnector implements AutoCloseable, ClosedStatusAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnector.class);
    private Properties properties;
    private Map<String, HikariDataSource> dataSources = new HashMap<>();
    private Map<String, SqlSessionFactory> sessionFactories = new HashMap<>();
    private AtomicBoolean closed = new AtomicBoolean(false);

    public DatabaseConnector(Properties properties) {
        this.properties = ConnectorProperties.filterProperties(properties, DatabaseConstants.PROP_KEY);
        LOGGER.info("Create DatabaseConnector with {}", this.properties);
        init();
    }

    private List<String> dsNames() {
        return properties.stringPropertyNames().stream()
                .filter(name -> name.endsWith(DatabaseConstants.JDBC_URL))
                .map(name -> name.substring(DatabaseConstants.PROP_KEY.length() + 1,
                        name.length() - DatabaseConstants.JDBC_URL.length()))
                .distinct()
                .filter(name ->
                        properties.containsKey(DatabaseConstants.PROP_KEY + "."
                                + name + DatabaseConstants.PASSWORD)
                                && properties.containsKey(DatabaseConstants.PROP_KEY + "."
                                + name + DatabaseConstants.USERNAME))
                .collect(Collectors.toList());
    }

    private void init() {
        List<String> dsNames = dsNames();
        dsNames.forEach(dsName -> {
            LOGGER.info("Create DataSource: {}", dsName);
            HikariDataSource dataSource = initDataSource(dsName);
            this.dataSources.put(dsName, dataSource);
            this.sessionFactories.put(dsName, initSessionFactory(dsName, dataSource));
        });
    }

    private HikariDataSource initDataSource(String dsName) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(propValue(dsName,
                DatabaseConstants.DATASOURCE_CLASS));
        config.setJdbcUrl(propValue(dsName,
                DatabaseConstants.JDBC_URL));
        config.setDriverClassName(propValue(dsName,
                DatabaseConstants.DRIVER_CLASS));
        config.setUsername(propValue(dsName,
                DatabaseConstants.USERNAME));
        config.setPassword(propValue(dsName,
                DatabaseConstants.PASSWORD));
        config.setAutoCommit(Boolean.parseBoolean(propValue(dsName,
                DatabaseConstants.AUTO_COMMIT)));
        config.setConnectionTimeout(Long.parseLong(propValue(dsName,
                DatabaseConstants.CONNECTION_TIMEOUT)));
        config.setIdleTimeout(Long.parseLong(propValue(dsName,
                DatabaseConstants.IDLE_TIMEOUT)));
        config.setMaxLifetime(Long.parseLong(propValue(dsName,
                DatabaseConstants.MAX_LIFETIME)));
        config.setMaximumPoolSize(Integer.parseInt(propValue(dsName,
                DatabaseConstants.MAX_POOL_SIZE)));
        config.setValidationTimeout(Long.parseLong(propValue(dsName,
                DatabaseConstants.VALIDATE_TIMEOUT)));
        config.setLeakDetectionThreshold(Long.parseLong(propValue(dsName,
                DatabaseConstants.LEAK_THRESHOLD)));
        config.setPoolName("hikari-" + dsName);
        config.setInitializationFailTimeout(Long.parseLong(propValue(dsName,
                DatabaseConstants.INIT_FAIL_TIMEOUT)));
        String isolation = propValue(dsName, DatabaseConstants.TRANS_ISOLATION);
        if (isolation != null && !"".equals(isolation.trim())) {
            config.setTransactionIsolation(isolation);
        }
        return new HikariDataSource(config);
    }

    private String propValue(String dsName, String key) {
        String name = DatabaseConstants.PROP_KEY + "." + dsName + key;
        String value = properties.getProperty(name);
        if (value == null) {
            name = DatabaseConstants.PROP_KEY + key;
            value = properties.getProperty(name);
        }
        return value;
    }

    private SqlSessionFactory initSessionFactory(String dsName, DataSource dataSource) {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment(dsName, transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setCacheEnabled(Boolean.parseBoolean(propValue(dsName,
                DatabaseConstants.MYBATIS_CACHE_ENABLED)));
        configuration.setLazyLoadingEnabled(Boolean.parseBoolean(propValue(dsName,
                DatabaseConstants.MYBATIS_LAZYLOADING_ENABLED)));
        configuration.setAutoMappingBehavior(AutoMappingBehavior.valueOf(propValue(dsName,
                DatabaseConstants.MYBATIS_AUTOMAPPING_BEHAVIOR)));
        configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.valueOf(propValue(dsName,
                DatabaseConstants.MYBATIS_AUTOMAPPING_UNKNOWNCOLUMN_BEHAVIOR)));
        configuration.setDefaultExecutorType(ExecutorType.valueOf(propValue(dsName,
                DatabaseConstants.MYBATIS_DEFAULT_EXECUTOR_TYPE)));
        configuration.setMapUnderscoreToCamelCase(Boolean.parseBoolean(propValue(dsName,
                DatabaseConstants.MYBATIS_MAP_UNDERSCORE_TO_CAMELCASE)));
        configuration.setLocalCacheScope(LocalCacheScope.valueOf(propValue(dsName,
                DatabaseConstants.MYBATIS_LOCAL_CACHE_SCOPE)));
        configuration.setCallSettersOnNulls(Boolean.parseBoolean(propValue(dsName,
                DatabaseConstants.MYBATIS_CALL_SETTERS_ON_NULLS)));
        String packages = propValue(dsName, DatabaseConstants.MYBATIS_MAPPERS_PACKAGES);
        for (String packageName : arrayParams(packages)) {
            LOGGER.info("Add mapper package {} for DataSource {}", packageName, dsName);
            configuration.addMappers(packageName);
        }
        String configs = propValue(dsName, DatabaseConstants.MYBATIS_MAPPERS_CONFIG);
        for (String config : arrayParams(configs)) {
            try {
                LOGGER.info("Add mapper config {} for DataSource {}", config, dsName);
                InputStream inputStream = Resources.getResourceAsStream(config);
                Configuration conf = builder.build(inputStream).getConfiguration();
                conf.getMappedStatements().forEach(configuration::addMappedStatement);
            } catch (IllegalArgumentException e1) {
                LOGGER.warn(e1.getMessage());
            } catch (Exception e2) {
                LOGGER.warn("DatabaseConnectorException", e2);
            }
        }
        return builder.build(configuration);
    }

    private static List<String> arrayParams(String param) {
        List<String> res = new ArrayList<>();
        if (param != null && !"".equals(param.trim())) {
            String[] params = param.split(",");
            if (params.length > 0) {
                res = Arrays.asList(params);
            }
        }
        return res;
    }

    public Connection getConnection() throws SQLException {
        return getConnection(DatabaseConstants.DEFAULT_DS_NAME);
    }

    public Connection getConnection(String dsName) throws SQLException {
        HikariDataSource dataSource = dataSources.get(dsName);
        if (dataSource == null) {
            throw new SQLException("DataSource " + dsName + " is not initialized.");
        }
        return dataSource.getConnection();
    }

    public DatabaseSession getSqlSession() {
        return getSqlSession(DatabaseConstants.DEFAULT_DS_NAME);
    }

    public DatabaseSession getSqlSession(boolean autoCommit) {
        return getSqlSession(DatabaseConstants.DEFAULT_DS_NAME, autoCommit);
    }

    public DatabaseSession getSqlSession(String dsName) {
        SqlSessionFactory sessionFactory = sessionFactories.get(dsName);
        if (sessionFactory == null) {
            throw new RuntimeException("SessionFactory " + dsName + " is not initialized.");
        }
        return new DatabaseSession(sessionFactory.openSession());
    }

    public DatabaseSession getSqlSession(String dsName, boolean autoCommit) {
        SqlSessionFactory sessionFactory = sessionFactories.get(dsName);
        if (sessionFactory == null) {
            throw new RuntimeException("SessionFactory " + dsName + " is not initialized.");
        }
        return new DatabaseSession(sessionFactory.openSession(autoCommit));
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            LOGGER.info("DatabaseConnector is closing...");
            dataSources.forEach((dsName, dataSource) -> {
                LOGGER.info("DataSource {} is closing...", dsName);
                dataSource.close();
                LOGGER.info("DataSource {} closed.", dsName);
            });
            dataSources.clear();
            LOGGER.info("DatabaseConnector closed.");
        }
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }
}

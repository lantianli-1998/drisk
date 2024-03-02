package com.roy.drisk.connector.database;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 数据库连接器常量
 */
public final class DatabaseConstants {
    public static final String PROP_KEY = "database";

    public static final String DATASOURCE_CLASS = ".dataSourceClassName";
    public static final String JDBC_URL = ".jdbcUrl";
    public static final String DRIVER_CLASS = ".driverClassName";
    public static final String USERNAME = ".username";
    public static final String PASSWORD = ".password";
    public static final String AUTO_COMMIT = ".autoCommit";
    public static final String CONNECTION_TIMEOUT = ".connectionTimeout";
    public static final String IDLE_TIMEOUT = ".idleTimeout";
    public static final String MAX_LIFETIME = ".maxLifetime";
    public static final String MAX_POOL_SIZE = ".maximumPoolSize";
    public static final String VALIDATE_TIMEOUT = ".validationTimeout";
    public static final String LEAK_THRESHOLD = ".leakDetectionThreshold";
    public static final String INIT_FAIL_TIMEOUT = ".initializationFailTimeout";
    public static final String TRANS_ISOLATION = ".transactionIsolation";

    public static final String DEFAULT_DS_NAME = "default";

    public static final String MYBATIS_CACHE_ENABLED = ".mybatisCacheEnabled";
    public static final String MYBATIS_LAZYLOADING_ENABLED = ".mybatisLazyLoadingEnabled";
    public static final String MYBATIS_AUTOMAPPING_BEHAVIOR = ".mybatisAutoMappingBehavior";
    public static final String MYBATIS_AUTOMAPPING_UNKNOWNCOLUMN_BEHAVIOR = ".mybatisAutoMappingUnknownColumnBehavior";
    public static final String MYBATIS_DEFAULT_EXECUTOR_TYPE = ".mybatisDefaultExecutorType";
    public static final String MYBATIS_MAP_UNDERSCORE_TO_CAMELCASE = ".mybatisMapUnderscoreToCamelCase";
    public static final String MYBATIS_LOCAL_CACHE_SCOPE = ".mybatisLocalCacheScope";
    public static final String MYBATIS_CALL_SETTERS_ON_NULLS = ".mybatisCallSettersOnNulls";
    public static final String MYBATIS_MAPPERS_PACKAGES = ".mybatisMappersPackages";
    public static final String MYBATIS_MAPPERS_CONFIG = ".mybatisMappersConfig";

    private DatabaseConstants() {
    }
}

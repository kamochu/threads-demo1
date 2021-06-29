package tech.meliora.natujenge.threads.datasource.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import tech.meliora.natujenge.threads.datasource.DataSourceConfig;
import tech.meliora.natujenge.threads.datasource.DataSourceManager;

import javax.sql.DataSource;

public class HikariDataSourceManager implements DataSourceManager {

    private DataSource dataSource;

    @Override
    public DataSource getDataSource(DataSourceConfig conf) {

        if(dataSource == null){

            HikariConfig config = new HikariConfig();

            HikariDataSource ds;

            String jdbcURL = conf.getJdbcUrl();

            config.setJdbcUrl(jdbcURL);

            config.setUsername(conf.getUsername());
            config.setPassword(conf.getPassword());
            config.setDriverClassName(conf.getDriverClassName());
            config.addDataSourceProperty("cachePrepStmts", conf.isCachePrepStmts() ? "true" : "false");
            config.addDataSourceProperty("useSSL", conf.isUseSSL() ? "true" : "false");

            if (conf.getPrepStmtCacheSize() > 0) {
                config.addDataSourceProperty("prepStmtCacheSize", conf.getPrepStmtCacheSize());
            }

            if (conf.getPrepStmtCacheSqlLimit() > 0) {
                config.addDataSourceProperty("prepStmtCacheSqlLimit", conf.getPrepStmtCacheSqlLimit());
            }

            if (conf.getMinimumIdle() > 0) {
                config.setMinimumIdle(conf.getMinimumIdle());
            }

            if (conf.getMaximumPoolSize() > 0) {
                config.setMaximumPoolSize(conf.getMaximumPoolSize());
            }

            ds = new HikariDataSource(config);

            dataSource = ds;

        }

        return dataSource;

    }
}

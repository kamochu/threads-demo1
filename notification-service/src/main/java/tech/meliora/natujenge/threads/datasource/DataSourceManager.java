package tech.meliora.natujenge.threads.datasource;

import javax.sql.DataSource;

public interface DataSourceManager {

    public DataSource getDataSource(DataSourceConfig dataSourceConfig);

}

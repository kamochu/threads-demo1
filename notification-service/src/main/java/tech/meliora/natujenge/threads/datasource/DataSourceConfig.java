package tech.meliora.natujenge.threads.datasource;

/**
 *
 * @author kamochu
 */
public class DataSourceConfig {

    private String id;
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
    private boolean cachePrepStmts;
    private int prepStmtCacheSize;
    private int prepStmtCacheSqlLimit;
    private boolean useSSL;
    private int minimumIdle;
    private int maximumPoolSize;
    private boolean isAutoCommit;
    private int connectTimeout;
    private int idleTimeout;
    private boolean isReadOnly;

    public DataSourceConfig() {
    }

    public DataSourceConfig(String id, String driverClassName, String jdbcUrl, String username, String password, boolean cachePrepStmts, int prepStmtCacheSize, int prepStmtCacheSqlLimit, int minimumIdle) {
        this.id = id;
        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.cachePrepStmts = cachePrepStmts;
        this.prepStmtCacheSize = prepStmtCacheSize;
        this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
        this.minimumIdle = minimumIdle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCachePrepStmts() {
        return cachePrepStmts;
    }

    public void setCachePrepStmts(boolean cachePrepStmts) {
        this.cachePrepStmts = cachePrepStmts;
    }

    public int getPrepStmtCacheSize() {
        return prepStmtCacheSize;
    }

    public void setPrepStmtCacheSize(int prepStmtCacheSize) {
        this.prepStmtCacheSize = prepStmtCacheSize;
    }

    public int getPrepStmtCacheSqlLimit() {
        return prepStmtCacheSqlLimit;
    }

    public void setPrepStmtCacheSqlLimit(int prepStmtCacheSqlLimit) {
        this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public int getMinimumIdle() {
        return minimumIdle;
    }

    public void setMinimumIdle(int minimumIdle) {
        this.minimumIdle = minimumIdle;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public boolean isIsAutoCommit() {
        return isAutoCommit;
    }

    public void setIsAutoCommit(boolean isAutoCommit) {
        this.isAutoCommit = isAutoCommit;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public boolean isIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    @Override
    public String toString() {
        return "DataSourceConfig{" + "id=" + id + ", driverClassName=" + driverClassName + ", jdbcUrl=" + jdbcUrl + ", username=" + username + ", password=" + password + ", cachePrepStmts=" + cachePrepStmts + ", prepStmtCacheSize=" + prepStmtCacheSize + ", prepStmtCacheSqlLimit=" + prepStmtCacheSqlLimit + ", useSSL=" + useSSL + ", minimumIdle=" + minimumIdle + ", maximumPoolSize=" + maximumPoolSize + ", isAutoCommit=" + isAutoCommit + ", connectTimeout=" + connectTimeout + ", idleTimeout=" + idleTimeout + ", isReadOnly=" + isReadOnly + '}';
    }

}

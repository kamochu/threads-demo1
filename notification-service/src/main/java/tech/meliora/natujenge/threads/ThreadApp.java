package tech.meliora.natujenge.threads;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import tech.meliora.natujenge.threads.datasource.DataSourceConfig;
import tech.meliora.natujenge.threads.datasource.DataSourceManager;
import tech.meliora.natujenge.threads.datasource.impl.HikariDataSourceManager;
import tech.meliora.natujenge.threads.repository.OrderRepository;
import tech.meliora.natujenge.threads.sendsms.SMSSender;
import tech.meliora.natujenge.threads.sendsms.impl.MelioraHTTPSMSSender;

import javax.sql.DataSource;

public class ThreadApp {

    private final static Logger logger = Logger.getLogger("main");

    public static void main(String[] args) throws Exception {

        BasicConfigurator.configure();

        logger.info("system|starting our thread app");

        logger.info("system|initializing datasource...");

        /**
         * Part 1: Data source initialization
         *
         * we have to create a data source to be used...
         *
         */
        String id = "MAIN";
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://51.15.211.168:3306/demo1";
        String username = "demo";
        String password = "Demo12$1";
        boolean cachePrepStmts = true;
        int prepStmtCacheSize = 250;
        int prepStmtCacheSqlLimit = 2048;
        int minimumIdle = 30;
        DataSourceConfig dataSourceConfig = new DataSourceConfig(id, driverClassName, jdbcUrl, username, password,
                cachePrepStmts, prepStmtCacheSize, prepStmtCacheSqlLimit, minimumIdle);
        DataSourceManager dataSourceManager = new HikariDataSourceManager();
        DataSource dataSource = dataSourceManager.getDataSource(dataSourceConfig);

        logger.info("system|finished initializing dataSource");


        /**
         * Part 2: Order repository instance
         *
         * This is required by the order processor (update orders) and poller (read new orders - read batch)
         */
        OrderRepository orderRepository = new OrderRepository(dataSource);
        logger.info("system|finished initializing orderRepository");

        /**
         * Part 3: Order processor initialization
         *
         * required by order repository to send sms
         *
         */
        String sendSMSEndpoint = "https://onehop-api.meliora.co.ke/api/messaging/sendsms";
        String apiKey = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDEiLCJvaWQiOjEwMSwidWlkIjoiN2Y3Nm" +
                "FjYzUtYjYxZi00YWFlLWE1ZTAtNjFmMDZlODcxM2Y1IiwiYXBpZCI6MjEsImlhdCI6MTYyNDk1ODM0NSw" +
                "iZXhwIjoxOTY0OTU4MzQ1fQ.oN77l1LEIVIg5KK8hPrTI50iTuTUeeb3RpLGZiJTauaBjVak9J4yc4jtsi_lA" +
                "VL7UY6gkNnIEaeEsNW-4xvR3w";
        SMSSender smsSender = new MelioraHTTPSMSSender(sendSMSEndpoint, apiKey);
        OrderProcessor orderProcessor = new OrderProcessor(orderRepository, smsSender);
        logger.info("system|finished initializing orderProcessor");

        /**
         *  Part 4: poller initialization
         *
         *  Our processing begins with polling...
         *  The poller has an executor service with 3 threads that is able to process order concurrently
         *
         */
        int sleepTime = 1000; // 1second
        int batchSize = 10; // 10 records per batch
        int executorServiceThreadCount = 3; // 3 threads to execute the
        Poller poller = new Poller(sleepTime, batchSize, executorServiceThreadCount, orderRepository, orderProcessor);
        logger.info("system|finished initializing poller");

        /**
         * Part 5: Starting the threads
         */
        Thread pollerThread = new Thread(poller, "poller");
        pollerThread.start();
        logger.info("system|finished starting poller thread");

        /**
         * Part 6: Register shutdown hook
         */
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-hook") {

            @Override
            public void run() {

                logger.info("system|received shut down sign");

                pollerThread.interrupt();
                logger.info("system|interrupted poller thread to stop");
            }
        });

    }


}

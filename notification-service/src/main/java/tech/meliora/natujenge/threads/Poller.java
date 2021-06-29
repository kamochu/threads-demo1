package tech.meliora.natujenge.threads;

import org.apache.log4j.Logger;
import tech.meliora.natujenge.threads.domain.Order;
import tech.meliora.natujenge.threads.repository.OrderRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Poller implements Runnable {

    private final Logger logger = Logger.getLogger(OrderProcessor.class);

    //internal processing parameters
    int lastRecordId = 0;


    //poller configs
    private long pollSleepTime;
    private int batchSize;
    private int executorServiceThreadsCount;

    //dependencies to be injected
    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor;

    private ExecutorService executorService;

    public Poller(long pollSleepTime, int batchSize, int executorServiceThreadsCount,
                  OrderRepository orderRepository, OrderProcessor orderProcessor) {
        this.pollSleepTime = pollSleepTime;
        this.batchSize = batchSize;
        this.executorServiceThreadsCount = executorServiceThreadsCount;

        this.orderProcessor = orderProcessor;
        this.orderRepository = orderRepository;

        logger.error("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                + "|executorServiceThreadsCount: " + executorServiceThreadsCount + "|initializing poller");

        executorService = Executors.newFixedThreadPool(this.executorServiceThreadsCount);
        logger.error("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                + "|initialized the executor service threads");
    }


    private void stop() {

        logger.error("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                + "|stopping the executor service");

        //shut down the executor
        this.executorService.shutdown();

        logger.error("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                + "|stopping poller");
    }

    @Override
    public void run() {

        while (true) {

            try {

                Thread.sleep(this.pollSleepTime);

                logger.debug("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                        + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                        + "|sleeping for " + this.pollSleepTime + "ms");

                List<Order> orders = this.orderRepository.poll(batchSize, lastRecordId);


                if (!orders.isEmpty()) {

                    logger.info("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                            + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                            + "|records: " + orders.size() + "|no records polled");

                    //create a count down latch for thread co-ordination
                    // (poller thread should wait for the batch to complete) before reading next batch
                    final CountDownLatch latch = new CountDownLatch(orders.size());

                    for (Order order : orders) {

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {

                                long start = System.currentTimeMillis();

                                try {
                                    logger.debug("transaction|orderId: " + order.getId()
                                            + "|msisdn:" + order.getPhoneNumber()
                                            + "|product: " + order.getProduct() + "|begin processing order");

                                    orderProcessor.process(order);

                                    logger.debug("transaction|orderId: " + order.getId()
                                            + "|msisdn:" + order.getPhoneNumber()
                                            + "|product: " + order.getProduct() + "|processed order");

                                } catch (Exception ex) {
                                    logger.error("transaction|orderId: " + order.getId()
                                            + "|msisdn:" + order.getPhoneNumber()
                                            + "|product: " + order.getProduct() + "|error processing order", ex);
                                } finally {
                                    //IMPORTANT to be in finally block
                                    // otherwise the poller thread will be blocked permanently in case of exceptions
                                    latch.countDown();

                                    long procesingTime = System.currentTimeMillis() - start;
                                    logger.debug("transaction|orderId: " + order.getId()
                                            + "|msisdn:" + order.getPhoneNumber()
                                            + "|product: " + order.getProduct()
                                            + "|processingTime: " + procesingTime + "|end processing order");
                                }
                            }
                        });


                        this.lastRecordId = order.getId(); // update current id as last record id
                    }


                    if (latch != null) {

                        logger.info("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                                + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                                + "|records: " + orders.size()
                                + "|WAITING-FOR-BATCH-TO-FINISH|counter: " + latch.getCount());

                        latch.await();

                        logger.info("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                                + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                                + "|records: " + orders.size()
                                + "|FINISHED-PROCESSING-THE-BATCH||counter: " + latch.getCount());
                    }

                } else {
                    logger.debug("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                            + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                            + "|records: " + orders.size() + "|no records polled");
                }

            } catch (InterruptedException ex) {
                logger.info("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                        + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                        + "|received an interrupt");
                break;

            } catch (Exception ex) {

                logger.error("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                        + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                        + "|error polling", ex);

                //Note: an error will not stop the poller thread...
            }

        }
        logger.info("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                + "|poller shutting down...");

        stop();

        logger.info("system|pollSleepTime: " + pollSleepTime + "|batchSize:" + batchSize
                + "|executorServiceThreadsCount: " + executorServiceThreadsCount
                + "|poller shut down");
    }
}

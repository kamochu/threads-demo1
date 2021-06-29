package tech.meliora.natujenge.threads;

import org.apache.log4j.Logger;
import tech.meliora.natujenge.threads.domain.Order;
import tech.meliora.natujenge.threads.enumeration.OrderStatus;
import tech.meliora.natujenge.threads.errors.SendSMSException;
import tech.meliora.natujenge.threads.repository.OrderRepository;
import tech.meliora.natujenge.threads.sendsms.SMSSender;
import tech.meliora.natujenge.threads.util.OrderUtil;

import java.sql.SQLException;

public class OrderProcessor {

    //our logger
    private Logger logger = Logger.getLogger(OrderProcessor.class);

    //objects to be injected to the order processor
    private OrderRepository orderRepository;
    private SMSSender smsSender;

    public OrderProcessor(OrderRepository orderRepository, SMSSender smsSender) {
        this.orderRepository = orderRepository;
        this.smsSender = smsSender;
    }

    public void process(Order order) throws SendSMSException, SQLException {
        //generate sms
        String sms = OrderUtil.generateSMS(order);
        String msisdn = order.getPhoneNumber();
        logger.info("transaction|orderId: " + order.getId() + "|msisdn:" + msisdn
                + "|sms: " + sms + "|generated sms");

        //send sms
        this.smsSender.sendSMS(msisdn, sms);
        logger.info("transaction|orderId: " + order.getId() + "|msisdn:" + msisdn
                + "|sms: " + sms + "|sent sms");

        //set order status to processed and save...
        order.setStatus(OrderStatus.PROCESSED);
        this.orderRepository.save(order);
    }
}

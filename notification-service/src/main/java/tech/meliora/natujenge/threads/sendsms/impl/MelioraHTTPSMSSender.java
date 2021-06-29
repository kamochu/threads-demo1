package tech.meliora.natujenge.threads.sendsms.impl;

import org.apache.log4j.Logger;
import tech.meliora.natujenge.threads.OrderProcessor;
import tech.meliora.natujenge.threads.domain.Order;
import tech.meliora.natujenge.threads.errors.SendSMSException;
import tech.meliora.natujenge.threads.sendsms.SMSSender;

import java.util.UUID;

public class MelioraHTTPSMSSender implements SMSSender {

    private Logger logger = Logger.getLogger(OrderProcessor.class);

    @Override
    public String sendSMS(String msisdn, String message) throws SendSMSException {

        String dummyMessageId = UUID.randomUUID().toString();

        logger.info("transaction|msisdn: " + msisdn + "|sms: " + message
                + "|messageId: " + dummyMessageId + "|logic not yet implemented.");

        return dummyMessageId;
    }
}

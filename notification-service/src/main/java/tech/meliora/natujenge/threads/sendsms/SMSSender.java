package tech.meliora.natujenge.threads.sendsms;

import tech.meliora.natujenge.threads.domain.Order;
import tech.meliora.natujenge.threads.errors.SendSMSException;

import java.io.IOException;

public interface SMSSender {
    /**
     * sends sms and returns message id from the sms gateway
     *
     * @param msisdn mobile number
     * @param message message to be sent out
     * @return message id from external sms gateway
     *
     * @throws SendSMSException
     */
    public String sendSMS(String msisdn, String message) throws SendSMSException, IOException;
}

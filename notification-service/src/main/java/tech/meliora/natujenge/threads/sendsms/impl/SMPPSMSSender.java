package tech.meliora.natujenge.threads.sendsms.impl;

import tech.meliora.natujenge.threads.domain.Order;
import tech.meliora.natujenge.threads.errors.SendSMSException;
import tech.meliora.natujenge.threads.sendsms.SMSSender;

public class SMPPSMSSender implements SMSSender {

    @Override
    public String sendSMS(String msisdn, String message) throws SendSMSException {
        throw new SendSMSException("logic not implemented");
    }
}

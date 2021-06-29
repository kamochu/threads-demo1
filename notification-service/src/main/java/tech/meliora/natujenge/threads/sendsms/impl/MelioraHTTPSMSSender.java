package tech.meliora.natujenge.threads.sendsms.impl;

import okhttp3.*;
import org.apache.log4j.Logger;
import tech.meliora.natujenge.threads.OrderProcessor;
import tech.meliora.natujenge.threads.domain.Message;
import tech.meliora.natujenge.threads.errors.SendSMSException;
import tech.meliora.natujenge.threads.sendsms.SMSSender;
import tech.meliora.natujenge.threads.sendsms.http.HTTPClient;
import tech.meliora.natujenge.threads.sendsms.http.HTTPResponse;
import tech.meliora.natujenge.threads.util.OkHttpClientUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MelioraHTTPSMSSender implements SMSSender {

    private Logger logger = Logger.getLogger(OrderProcessor.class);

    private String endpont;
    private String apiKey;

    public MelioraHTTPSMSSender(String endpont, String apiKey) {
        this.endpont = endpont;
        this.apiKey = apiKey;
    }

    @Override
    public String sendSMS(String msisdn, String message) throws SendSMSException, IOException, NoSuchAlgorithmException, KeyManagementException {

        // this shall be passed as refId in the request
        String dummyMessageId = UUID.randomUUID().toString();

        Message composedMessage = new Message("SMSAfrica", msisdn, message, dummyMessageId);

        logger.info("transaction|msisdn: " + msisdn + "|sms: " + message
                + "|composedMessage: " + composedMessage + "|about to send message.");

        //BAD WAY of doing this... replace with Jackson Object Mapper
        String content = "{\"to\":\"" + composedMessage.getTo() + "\", \"from\": \""
                + composedMessage.getFrom() + "\", \"message\" : \"" + composedMessage.getMessage()
                + "\", \"refId\": \"" + composedMessage.getRefId() + "\"}";


        logger.info("transaction|msisdn: " + msisdn + "|sms: " + message
                + "|requestBody: " + content + "|json body.");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + this.apiKey);
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        HTTPResponse response = HTTPClient.send(endpont, content, "POST", "application/json; charset=utf-8", headers, 2000, 10000);

        logger.info("transaction|msisdn: " + msisdn + "|sms: " + message
                + "|response: " + response + "|response.");

        return response.getBody();

    }


}

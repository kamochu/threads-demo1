package tech.meliora.natujenge.threads.sendsms.impl;

import okhttp3.*;
import org.apache.log4j.Logger;
import tech.meliora.natujenge.threads.OrderProcessor;
import tech.meliora.natujenge.threads.domain.Message;
import tech.meliora.natujenge.threads.domain.Order;
import tech.meliora.natujenge.threads.errors.SendSMSException;
import tech.meliora.natujenge.threads.sendsms.SMSSender;

import java.io.IOException;
import java.util.UUID;

public class MelioraHTTPSMSSender implements SMSSender {

    private Logger logger = Logger.getLogger(OrderProcessor.class);

    @Override
    public String sendSMS(String msisdn, String message) throws SendSMSException, IOException {

        // request media type
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // onehop endpoint
        String url = "https://onehop-api.meliora.co.ke/api/messaging/sendsms";

        String apiKey = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDEiLCJvaWQiOjEwMSwidWlkIjoiN2Y3Nm" +
                "FjYzUtYjYxZi00YWFlLWE1ZTAtNjFmMDZlODcxM2Y1IiwiYXBpZCI6MjEsImlhdCI6MTYyNDk1ODM0NSw" +
                "iZXhwIjoxOTY0OTU4MzQ1fQ.oN77l1LEIVIg5KK8hPrTI50iTuTUeeb3RpLGZiJTauaBjVak9J4yc4jtsi_lA" +
                "VL7UY6gkNnIEaeEsNW-4xvR3w";

        OkHttpClient client = new OkHttpClient();

        // this shall be passed as refId in the request
        String dummyMessageId = UUID.randomUUID().toString();

        Message composedMessage = new Message("SMSAfrica",msisdn,message,dummyMessageId);

        logger.info("transaction|msisdn: " + msisdn + "|sms: " + message
                + "|messageId: " + dummyMessageId + "|about to send message.");

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(composedMessage));

        // prepare the request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization","Bearer "+apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response = client.newCall(request).execute();

        return response.body().string();

    }
}

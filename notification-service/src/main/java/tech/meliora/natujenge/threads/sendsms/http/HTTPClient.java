package tech.meliora.natujenge.threads.sendsms.http;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author kamochu
 */
public class HTTPClient {

    public static HTTPResponse send(String url, String request, String method, String contentType, Map<String, String> headers, int connectTimeout, int readTimeout) throws MalformedURLException, ProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {

        StringBuilder response;

        //read the response
        int responseCode;

//        https = (url != null && url.contains("https"));
        if (url.contains("https")) {
            // https

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL obj = new URL(url);

            HttpsURLConnection connection;

            connection = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            if (headers != null) {
                headers.entrySet().forEach((entry) -> {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    connection.setRequestProperty(key, value);
                });
            }

            // Send post request
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(request);
                wr.flush();
            }

            responseCode = connection.getResponseCode();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        } else {
            // http

            URL obj = new URL(url);

            HttpURLConnection connection;

            connection = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            if (headers != null) {
                headers.entrySet().forEach((entry) -> {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    connection.setRequestProperty(key, value);
                });
            }

            // Send post request
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(request);
                wr.flush();
            }

            responseCode = connection.getResponseCode();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        }

        return new HTTPResponse(responseCode, response.toString());

    }

}

package com.sleet.api.service;

import com.sleet.api.HttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Service} implementation that provides methods to view account info
 * and place orders via the TD API. Any API calls from this service will require
 * an authorization grant.
 *
 * @author mautomic
 */
public class TradingService extends Service {

    private static final Logger LOG = LoggerFactory.getLogger(OptionService.class);
    private final static String ACCOUNTS = "accounts";
    private final static String AUTHORIZATION = "authorization";
    private final static String BEARER = "Bearer ";

    public TradingService() {
        httpClient = new HttpClient(DEFAULT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS);
    }

    public String getAccountInfo(final String accountNum, final String accessToken) throws Exception {
        final String url = API_URL + ACCOUNTS + "/" + accountNum;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(AUTHORIZATION, BEARER + accessToken);
        final Response response = httpClient.get(url, headerMap, DEFAULT_TIMEOUT_MILLIS);
        return response.getResponseBody();
    }

    //TODO: Implement order methods
    public void getOrder() {}

    public void cancelOrder() {}

    public void replaceOrder() {}

    public void placeOrder() {}

    public void getSavedOrder() {}

    public void createSavedOrder() {}

    public void deleteSavedOrder() {}
}

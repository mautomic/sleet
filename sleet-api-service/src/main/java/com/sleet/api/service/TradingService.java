package com.sleet.api.service;

import com.sleet.api.HttpClient;
import com.sleet.api.model.Order;
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
    private final static String ORDERS = "orders";
    private final static String SAVED_ORDERS = "savedorders";
    private final static String AUTHORIZATION = "authorization";
    private final static String BEARER = "Bearer ";
    private final static String CONTENT_TYPE = "Content-Type";
    private final static String APPLICATION_JSON = "application/json";

    public TradingService() {
        httpClient = new HttpClient(DEFAULT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS);
    }

    public String getAccountInfo(final String accountNum, final String accessToken) throws Exception {
        final String url = API_URL + ACCOUNTS + "/" + accountNum;
        final Map<String, String> headerMap = new HashMap<>();
        headerMap.put(AUTHORIZATION, BEARER + accessToken);
        final Response response = httpClient.get(url, headerMap, DEFAULT_TIMEOUT_MILLIS);
        return response.getResponseBody();
    }

    //TODO: Implement order methods
    public void getOrder() {
    }

    public void cancelOrder() {
    }

    public void replaceOrder() {
    }

    public Response placeOrder(final Order order, final String accountNum, final String accessToken) throws Exception {
        final String url = API_URL + ACCOUNTS + "/" + accountNum + "/" + ORDERS;
        final String orderJson = mapper.writeValueAsString(order);
        LOG.info("Placing order: " + orderJson);

        final Map<String, String> headerMap = new HashMap<>();
        headerMap.put(AUTHORIZATION, BEARER + accessToken);
        headerMap.put("Content-Type", "application/json");

        return httpClient.post(url, orderJson, headerMap, 5000);
    }

    public void getSavedOrder() {
    }

    /**
     * Create a saved order (will not be executed)
     *
     * @param order       to created
     * @param accountNum  to create order in
     * @param accessToken to authenticate with to save order
     * @throws Exception if there is an issue with creating a saved order
     */
    public Response createSavedOrder(final Order order, final String accountNum, final String accessToken) throws Exception {
        final String url = API_URL + ACCOUNTS + "/" + accountNum + "/" + SAVED_ORDERS;
        final String orderJson = mapper.writeValueAsString(order);
        LOG.info("Creating saved order: " + orderJson);

        final Map<String, String> headerMap = new HashMap<>();
        headerMap.put(AUTHORIZATION, BEARER + accessToken);
        headerMap.put(CONTENT_TYPE, APPLICATION_JSON);
        return httpClient.post(url, orderJson, headerMap, 5000);
    }

    public void deleteSavedOrder() {
    }
}

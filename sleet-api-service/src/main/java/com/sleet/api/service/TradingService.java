package com.sleet.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleet.api.HttpClient;
import com.sleet.api.model.Order;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.sleet.api.Constants.*;

/**
 * An TD API interface that provides methods to view account info
 * and place orders via the TD API. Any API calls from this service will require
 * an authorization grant.
 *
 * @author mautomic
 */
public class TradingService {

    private final HttpClient httpClient;

    private static final Logger LOG = LoggerFactory.getLogger(TradingService.class);
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    public TradingService(final HttpClient httpClient) {
        this.httpClient = httpClient;
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
        headerMap.put(CONTENT_TYPE, APPLICATION_JSON);

        return httpClient.post(url, orderJson, headerMap, DEFAULT_TIMEOUT_MILLIS);
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
        return httpClient.post(url, orderJson, headerMap, DEFAULT_TIMEOUT_MILLIS);
    }

    public void deleteSavedOrder() {
    }
}

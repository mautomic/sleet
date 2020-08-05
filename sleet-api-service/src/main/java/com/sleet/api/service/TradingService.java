package com.sleet.api.service;

import com.sleet.api.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public TradingService() {
        httpClient = new HttpClient(DEFAULT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS);
    }

    //TODO: Implement
    public void getPositions(final String accountNum, final String accessToken) {
        final String url = API_URL + ACCOUNTS + accountNum;
    }

    //TODO: Implement
    public void getBalances(final String accountNum, final String accessToken) {
        final String url = API_URL + ACCOUNTS + accountNum;
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

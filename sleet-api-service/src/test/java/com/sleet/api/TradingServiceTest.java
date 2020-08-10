package com.sleet.api;

import com.sleet.api.model.Instrument;
import com.sleet.api.model.Order;
import com.sleet.api.model.OrderLegCollection;
import com.sleet.api.service.TradingService;
import org.junit.Assert;
import org.junit.Test;

public class TradingServiceTest {

    @Test
    public void testAccountInfoRequest() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService();
        final String jsonAccountInfo = tradingService.getAccountInfo(accountNum, accessToken);
        System.out.println(jsonAccountInfo);
        Assert.assertNotNull(jsonAccountInfo);
    }

    @Test
    public void testCreateSavedOrder() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService();

        Instrument instrument = new Instrument("SPY", "EQUITY");
        OrderLegCollection orderLegCollection = new OrderLegCollection("BUY", 1, instrument);

        Order order = new Order("LIMIT", "NORMAL", "DAY", 1, 320, true, "SINGLE", orderLegCollection);

        tradingService.createSavedOrder(order, accountNum, accessToken);
    }
}

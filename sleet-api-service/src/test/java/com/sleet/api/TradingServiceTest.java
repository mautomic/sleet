package com.sleet.api;

import com.sleet.api.model.Instrument;
import com.sleet.api.model.Order;
import com.sleet.api.model.OrderLegCollection;
import com.sleet.api.service.TradingService;
import org.asynchttpclient.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<OrderLegCollection> legCollectionList = new ArrayList<>();
        OrderLegCollection orderLegCollection = new OrderLegCollection("BUY", 1, instrument);
        legCollectionList.add(orderLegCollection);

        Order order = new Order("LIMIT", "NORMAL", "DAY", 320, "SINGLE", "NONE", legCollectionList);

        Response response = tradingService.createSavedOrder(order, accountNum, accessToken);
        System.out.println(response.getResponseBody());
    }

    @Test
    public void testPlaceOrder() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService();

        Instrument instrument = new Instrument("SPY", "EQUITY");
        List<OrderLegCollection> legCollectionList = new ArrayList<>();
        OrderLegCollection orderLegCollection = new OrderLegCollection("BUY", 1, instrument);
        legCollectionList.add(orderLegCollection);

        Order order = new Order("LIMIT", "NORMAL", "DAY", 320, "SINGLE", "NONE", legCollectionList);

        Response response = tradingService.placeOrder(order, accountNum, accessToken);
        System.out.println(response.getResponseBody());
    }
}

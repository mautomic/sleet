package com.sleet.api.service;

import com.sleet.api.HttpClient;
import com.sleet.api.model.Instrument;
import com.sleet.api.model.Order;
import com.sleet.api.model.OrderLegCollection;
import org.asynchttpclient.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link TradingService}
 *
 * @author mautomic
 */
public class TradingServiceTest {

    @Test
    public void testAccountInfoRequest() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService(new HttpClient(5000, 5000));
        final String jsonAccountInfo = tradingService.getAccountInfo(accountNum, accessToken);
        System.out.println(jsonAccountInfo);
        Assert.assertNotNull(jsonAccountInfo);
    }

    @Test
    public void testCreateSavedOrder() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService(new HttpClient(5000, 5000));

        Instrument instrument = new Instrument("SPY", "EQUITY");
        List<OrderLegCollection> legCollectionList = new ArrayList<>();
        OrderLegCollection orderLegCollection = new OrderLegCollection("BUY", 1, instrument);
        legCollectionList.add(orderLegCollection);

        Order order = new Order("LIMIT", "NORMAL", "DAY", 320, "SINGLE", "NONE", legCollectionList);

        Response response = tradingService.createSavedOrder(order, accountNum, accessToken);
        System.out.println(response.getResponseBody());
    }

    @Test
    public void testPlaceEquityOrder() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService(new HttpClient(5000, 5000));

        Instrument instrument = new Instrument("SPY", "EQUITY");
        List<OrderLegCollection> legCollectionList = new ArrayList<>();
        OrderLegCollection orderLegCollection = new OrderLegCollection("BUY", 1, instrument);
        legCollectionList.add(orderLegCollection);

        Order order = new Order("LIMIT", "NORMAL", "DAY", 320, "SINGLE", "NONE", legCollectionList);

        Response response = tradingService.placeOrder(order, accountNum, accessToken);
        System.out.println(response.getResponseBody());
    }

    @Test
    public void testPlaceOptionOrder() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService(new HttpClient(5000, 5000));

        Instrument instrument = new Instrument("SPY_082120C335", "OPTION");

        OrderLegCollection orderLegCollection = new OrderLegCollection("BUY_TO_OPEN", 1, instrument);

        List<OrderLegCollection> legs = new ArrayList<>();
        legs.add(orderLegCollection);

        Order order = new Order("LIMIT", "NORMAL", "DAY", 2.10, "SINGLE", "NONE", legs);

        Response response = tradingService.placeOrder(order, accountNum, accessToken);
        System.out.println(response.getResponseBody());
    }

    @Test
    public void testPlaceOptionSpreadOrder() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService(new HttpClient(5000, 5000));

        Instrument buyOption = new Instrument("SPY_082120C335", "OPTION");
        Instrument sellOption = new Instrument("SPY_082120C340", "OPTION");

        OrderLegCollection buyLeg = new OrderLegCollection("BUY_TO_OPEN", 1, buyOption);
        OrderLegCollection sellLeg = new OrderLegCollection("SELL_TO_OPEN", 1, sellOption);

        List<OrderLegCollection> legs = Arrays.asList(buyLeg, sellLeg);

        Order order = new Order("NET_DEBIT", "NORMAL", "DAY", 1.20, "SINGLE", "VERTICAL", legs);

        Response response = tradingService.placeOrder(order, accountNum, accessToken);
        System.out.println(response.getResponseBody());
    }
}

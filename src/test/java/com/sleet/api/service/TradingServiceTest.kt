package com.sleet.api.service

import com.sleet.api.model.Instrument
import com.sleet.api.model.OrderLegCollection
import com.sleet.api.model.Order
import org.asynchttpclient.Dsl
import org.junit.Assert
import org.junit.Test
import kotlin.Throws
import java.lang.Exception
import java.util.ArrayList
import java.util.Arrays

/**
 * Test class for [TradingService]
 *
 * @author mautomic
 */
class TradingServiceTest {

    @Test
    @Throws(Exception::class)
    fun testAccountInfoRequest() {

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val jsonAccountInfo = tradingService.getAccountInfo(TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN)
        println(jsonAccountInfo)
        Assert.assertNotNull(jsonAccountInfo)
    }

    @Test
    @Throws(Exception::class)
    fun testCreateSavedOrder() {

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("SPY", "EQUITY")

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection("BUY", 1, instrument)
        legCollectionList.add(orderLegCollection)

        val order = Order("LIMIT", "NORMAL", "DAY", 320.0, "SINGLE", "NONE", legCollectionList)
        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testPlaceEquityOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("PLTR", "EQUITY")

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection("BUY", 1, instrument)

        legCollectionList.add(orderLegCollection)
        val order = Order("LIMIT", "NORMAL", "DAY", 22.0, "SINGLE", "NONE", legCollectionList)
        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, true)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testGetEquityOrder() {
        val orderId = "4191208491"

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val response = tradingService.getOrder(TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, orderId, true)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testReplaceEquityOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val orderId = "4191208491"

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("PLTR", "EQUITY")

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection("BUY", 1, instrument)

        legCollectionList.add(orderLegCollection)
        val order = Order("LIMIT", "NORMAL", "DAY", 21.5, "SINGLE", "NONE", legCollectionList)
        val response =
            tradingService.replaceOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, orderId, true)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testCancelEquityOrder() {

        val orderId = "4191342218"

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val response = tradingService.cancelOrder(TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, orderId, true)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testPlaceOptionOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("SPY_082120C335", "OPTION")

        val orderLegCollection = OrderLegCollection("BUY_TO_OPEN", 1, instrument)
        val legs: MutableList<OrderLegCollection> = ArrayList()
        legs.add(orderLegCollection)

        val order = Order("LIMIT", "NORMAL", "DAY", 2.10, "SINGLE", "NONE", legs)
        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, true)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testPlaceOptionSpreadOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val buyOption = Instrument("SPY_082120C335", "OPTION")
        val sellOption = Instrument("SPY_082120C340", "OPTION")

        val buyLeg = OrderLegCollection("BUY_TO_OPEN", 1, buyOption)
        val sellLeg = OrderLegCollection("SELL_TO_OPEN", 1, sellOption)
        val legs = Arrays.asList(buyLeg, sellLeg)

        val order = Order("NET_DEBIT", "NORMAL", "DAY", 1.20, "SINGLE", "VERTICAL", legs)
        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, true)
        println(response.responseBody)
    }
}
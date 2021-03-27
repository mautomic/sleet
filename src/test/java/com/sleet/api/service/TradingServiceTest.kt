package com.sleet.api.service

import com.sleet.api.model.*
import org.asynchttpclient.Dsl
import org.junit.Assert
import org.junit.Ignore
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
    @Ignore
    @Throws(Exception::class)
    fun testAccountInfoRequest() {

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val jsonAccountInfo = tradingService.getAccountInfo(TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN)
        println(jsonAccountInfo)
        Assert.assertNotNull(jsonAccountInfo)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testCreateSavedOrder() {

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("SPY", AssetType.EQUITY.name)

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection(Instruction.BUY.name, 1, instrument)
        legCollectionList.add(orderLegCollection)

        val order = TDOrder(OrderType.LIMIT.name, Session.NORMAL.name, Duration.DAY.name, 320.0,
            OrderStrategyType.SINGLE.name, ComplexOrderStrategyType.NONE.name, legCollectionList)

        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN)
        println(response.responseBody)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testPlaceEquityOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("PLTR", AssetType.EQUITY.name)

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection(Instruction.BUY.name, 1, instrument)

        legCollectionList.add(orderLegCollection)
        val order = TDOrder(OrderType.LIMIT.name, Session.NORMAL.name, Duration.DAY.name, 22.0,
            OrderStrategyType.SINGLE.name, ComplexOrderStrategyType.NONE.name, legCollectionList)

        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, true)
        println(response.responseBody)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testGetEquityOrder() {
        val orderId = "4191208491"

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val response = tradingService.getOrder(TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, orderId, true)
        println(response.responseBody)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testReplaceEquityOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val orderId = "4191208491"

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("PLTR", AssetType.EQUITY.name)

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection(Instruction.BUY.name, 1, instrument)

        legCollectionList.add(orderLegCollection)
        val order = TDOrder(OrderType.LIMIT.name, Session.NORMAL.name, Duration.DAY.name, 21.5,
            OrderStrategyType.SINGLE.name, ComplexOrderStrategyType.NONE.name, legCollectionList)

        val response =
            tradingService.replaceOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, orderId, true)
        println(response.responseBody)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testCancelEquityOrder() {

        val orderId = "4191342218"

        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val response = tradingService.cancelOrder(TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, orderId, true)
        println(response.responseBody)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testPlaceOptionOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("SPY_071621C400", AssetType.OPTION.name)

        val orderLegCollection = OrderLegCollection(Instruction.BUY_TO_OPEN.name, 1, instrument)
        val legs: MutableList<OrderLegCollection> = ArrayList()
        legs.add(orderLegCollection)

        val order = TDOrder(OrderType.LIMIT.name, Session.NORMAL.name, Duration.DAY.name, 2.10,
            OrderStrategyType.SINGLE.name, ComplexOrderStrategyType.NONE.name, legs)

        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, true)
        println(response.responseBody)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testPlaceOptionSpreadOrder() {

        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val buyOption = Instrument("SPY_071621C400", AssetType.OPTION.name)
        val sellOption = Instrument("SPY_071621C440", AssetType.OPTION.name)

        val buyLeg = OrderLegCollection(Instruction.BUY_TO_OPEN.name, 1, buyOption)
        val sellLeg = OrderLegCollection(Instruction.SELL_TO_OPEN.name, 1, sellOption)
        val legs = Arrays.asList(buyLeg, sellLeg)

        val order = TDOrder(OrderType.NET_DEBIT.name, Session.NORMAL.name, Duration.DAY.name, 1.20,
            OrderStrategyType.SINGLE.name, ComplexOrderStrategyType.VERTICAL.name, legs)

        val response = tradingService.placeOrder(order, TestConstants.ACCOUNT_NUM, TestConstants.ACCESS_TOKEN, true)
        println(response.responseBody)
    }
}
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

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        val accessToken = ""
        val accountNum = ""
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val jsonAccountInfo = tradingService.getAccountInfo(accountNum, accessToken)
        println(jsonAccountInfo)
        Assert.assertNotNull(jsonAccountInfo)
    }

    @Test
    @Throws(Exception::class)
    fun testCreateSavedOrder() {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        val accessToken = ""
        val accountNum = ""
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("SPY", "EQUITY")

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection("BUY", 1, instrument)
        legCollectionList.add(orderLegCollection)

        val order = Order("LIMIT", "NORMAL", "DAY", 320.0, "SINGLE", "NONE", legCollectionList)
        val response = tradingService.createSavedOrder(order, accountNum, accessToken)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testPlaceEquityOrder() {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val accessToken = ""
        val accountNum = ""
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("SPY", "EQUITY")

        val legCollectionList: MutableList<OrderLegCollection> = ArrayList()
        val orderLegCollection = OrderLegCollection("BUY", 1, instrument)

        legCollectionList.add(orderLegCollection)
        val order = Order("LIMIT", "NORMAL", "DAY", 320.0, "SINGLE", "NONE", legCollectionList)
        val response = tradingService.placeOrder(order, accountNum, accessToken)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testPlaceOptionOrder() {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val accessToken = ""
        val accountNum = ""
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val instrument = Instrument("SPY_082120C335", "OPTION")

        val orderLegCollection = OrderLegCollection("BUY_TO_OPEN", 1, instrument)
        val legs: MutableList<OrderLegCollection> = ArrayList()
        legs.add(orderLegCollection)

        val order = Order("LIMIT", "NORMAL", "DAY", 2.10, "SINGLE", "NONE", legs)
        val response = tradingService.placeOrder(order, accountNum, accessToken)
        println(response.responseBody)
    }

    @Test
    @Throws(Exception::class)
    fun testPlaceOptionSpreadOrder() {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        // BE CAREFUL: THIS WILL CREATE A REAL ORDER
        val accessToken = ""
        val accountNum = ""
        val tradingService = TradingService(Dsl.asyncHttpClient(Dsl.config()))
        val buyOption = Instrument("SPY_082120C335", "OPTION")
        val sellOption = Instrument("SPY_082120C340", "OPTION")

        val buyLeg = OrderLegCollection("BUY_TO_OPEN", 1, buyOption)
        val sellLeg = OrderLegCollection("SELL_TO_OPEN", 1, sellOption)
        val legs = Arrays.asList(buyLeg, sellLeg)

        val order = Order("NET_DEBIT", "NORMAL", "DAY", 1.20, "SINGLE", "VERTICAL", legs)
        val response = tradingService.placeOrder(order, accountNum, accessToken)
        println(response.responseBody)
    }
}
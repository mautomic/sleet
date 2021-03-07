package com.sleet.api.service

import com.sleet.api.RequestUtil.Companion.createGetRequest
import com.sleet.api.RequestUtil.Companion.createPostRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.sleet.api.Constants
import com.sleet.api.model.Order
import org.asynchttpclient.Response
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Request
import org.slf4j.LoggerFactory

import kotlin.Throws
import java.util.concurrent.TimeUnit
import java.lang.Exception

/**
 * An TD API interface that provides methods to view account info and place orders.
 * Any API calls from this service will require an authorization grant.
 *
 * @author mautomic
 */
class TradingService(private val httpClient: AsyncHttpClient) {

    companion object {
        private val LOG = LoggerFactory.getLogger(TradingService::class.java)
        private val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    }

    @Throws(Exception::class)
    fun getAccountInfo(accountNum: String, accessToken: String): String {
        val url: String = Constants.API_URL + Constants.ACCOUNTS + "/" + accountNum
        val headerMap: Map<String, String> = mapOf(
            Constants.AUTHORIZATION to Constants.BEARER + accessToken
        )
        val request = createGetRequest(url, headerMap)
        val response = httpClient.executeRequest(request)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
        return response.responseBody
    }

    //TODO: Implement order methods
    fun getOrder() {}

    fun cancelOrder() {}

    fun replaceOrder() {}

    @Throws(Exception::class)
    fun placeOrder(order: Order?, accountNum: String, accessToken: String): Response {
        val url: String = Constants.API_URL + Constants.ACCOUNTS + "/" + accountNum + "/" + Constants.ORDERS
        val request = createOrderRequest(url, order, accessToken)
        return httpClient.executeRequest(request)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    fun getSavedOrder() {}

    /**
     * Create a saved order (will not be executed)
     *
     * @param order       to created
     * @param accountNum  to create order in
     * @param accessToken to authenticate with to save order
     * @throws Exception if there is an issue with creating a saved order
     */
    @Throws(Exception::class)
    fun createSavedOrder(order: Order?, accountNum: String, accessToken: String): Response {
        val url: String = Constants.API_URL + Constants.ACCOUNTS + "/" + accountNum + "/" + Constants.SAVED_ORDERS
        val request = createOrderRequest(url, order, accessToken)
        return httpClient.executeRequest(request)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    fun deleteSavedOrder() {}

    private fun createOrderRequest(url: String, order: Order?, accessToken: String): Request? {
        val orderJson = mapper.writeValueAsString(order)
        LOG.info("Creating order with schema:\n $orderJson")

        val headerMap: Map<String, String> = mapOf(
            Constants.AUTHORIZATION to Constants.BEARER + accessToken,
            Constants.CONTENT_TYPE to Constants.APPLICATION_JSON
        )
        return createPostRequest(url, orderJson, headerMap)
    }
}
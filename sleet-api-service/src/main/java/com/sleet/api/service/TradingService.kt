package com.sleet.api.service

import com.sleet.api.RequestUtil.Companion.createGetRequest
import com.sleet.api.RequestUtil.Companion.createPostRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.sleet.api.Constants.API_URL
import com.sleet.api.Constants.ACCOUNTS
import com.sleet.api.Constants.AUTHORIZATION
import com.sleet.api.Constants.APPLICATION_JSON
import com.sleet.api.Constants.BEARER
import com.sleet.api.Constants.CONTENT_TYPE
import com.sleet.api.Constants.DEFAULT_TIMEOUT_MILLIS
import com.sleet.api.Constants.ORDERS
import com.sleet.api.Constants.QUERY_PARAM_FROM_ENTERED_TIME
import com.sleet.api.Constants.QUERY_PARAM_STATUS
import com.sleet.api.Constants.QUERY_PARAM_TO_ENTERED_TIME
import com.sleet.api.Constants.SAVED_ORDERS
import com.sleet.api.Constants.SLASH
import com.sleet.api.RequestUtil.Companion.createDeleteRequest
import com.sleet.api.RequestUtil.Companion.createPutRequest
import com.sleet.api.model.Order
import org.apache.commons.lang3.StringUtils
import org.asynchttpclient.Response
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Request

import kotlin.Throws
import java.util.concurrent.TimeUnit
import java.lang.Exception

/**
 * An TD API interface that provides methods to view account info and place orders.
 * Any API calls with this service will require an access token to be authenticated.
 *
 * @author mautomic
 */
class TradingService(private val httpClient: AsyncHttpClient) {

    companion object {
        private val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    }

    /**
     * Get information for a TD Ameritrade trading account
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/get/accounts/%7BaccountId%7D-0">Get Account</a>
     *
     * @param accountNum  to retrieve info for
     * @param accessToken to authenticate with
     * @return a [Response] with the HTTP status and body
     * @throws Exception if there is an issue creating or executing the GET request
     */
    @Throws(Exception::class)
    fun getAccountInfo(accountNum: String, accessToken: String): Response {
        val url: String = API_URL + ACCOUNTS + SLASH + accountNum
        val headerMap: Map<String, String> = mapOf(
            AUTHORIZATION to BEARER + accessToken
        )
        val request = createGetRequest(url, headerMap)
        return httpClient.executeRequest(request)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Get a specific existing order. Unless the realOrder flag is explicitly overridden, this function is
     * set to retrieve saved orders as a safety mechanism.
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/get/accounts/%7BaccountId%7D/orders/%7BorderId%7D-0">Get Order</a>
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/get/accounts/%7BaccountId%7D/savedorders/%7BsavedOrderId%7D-0">Get Saved Order</a>
     *
     * @param accountNum  to create order in
     * @param accessToken to authenticate with
     * @param orderId     of order to retrieve
     * @param realOrder   true indicates a real working order, false indicates a saved order
     * @return a [Response] with the HTTP status and body
     * @throws Exception if there is an issue creating or executing the GET request
     */
    @Throws(Exception::class)
    fun getOrder(accountNum: String, accessToken: String, orderId: String, realOrder: Boolean = false): Response {
        val url: String =
            if (realOrder) API_URL + ACCOUNTS + SLASH + accountNum + SLASH + ORDERS + SLASH + orderId
            else API_URL + ACCOUNTS + SLASH + accountNum + SLASH + SAVED_ORDERS + SLASH + orderId

        val headerMap: Map<String, String> = mapOf(
            AUTHORIZATION to BEARER + accessToken
        )
        val request = createGetRequest(url, headerMap)
        return httpClient.executeRequest(request)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Get all real orders for this account between provided dates. By default, all types of order statuses are returned
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/get/accounts/%7BaccountId%7D/orders-0">Get Orders By Path</a>
     *
     * @param accountNum  to retrieve orders from
     * @param accessToken to authenticate with
     * @param fromEnteredTime to indicate up till how far back to retrieve existing orders
     * @param toEnteredTime to indicate up till how recently to retrieve existing orders
     * @param status of types of orders to query for (ie, FILLED, WORKING, ...)
     * @return a [Response] with the HTTP status and body
     * @throws Exception if there is an issue creating or executing the GET request
     */
    @Throws(Exception::class)
    fun getAllOrders(
        accountNum: String,
        accessToken: String,
        fromEnteredTime: String,
        toEnteredTime: String,
        status: String = StringUtils.EMPTY
    ): Response {

        var url: String = API_URL + ACCOUNTS + SLASH + accountNum + SLASH + ORDERS +
                "?$QUERY_PARAM_FROM_ENTERED_TIME$fromEnteredTime$QUERY_PARAM_TO_ENTERED_TIME$toEnteredTime"

        if (!StringUtils.isEmpty(status))
            url += QUERY_PARAM_STATUS + status

        val headerMap: Map<String, String> = mapOf(
            AUTHORIZATION to BEARER + accessToken
        )
        val request = createGetRequest(url, headerMap)
        return httpClient.executeRequest(request)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Get all saved orders for this account
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/get/accounts/%7BaccountId%7D/savedorders/%7BsavedOrderId%7D-0">Get Saved Orders By Path</a>
     *
     * @param accountNum  to retrieve saved orders from
     * @param accessToken to authenticate with
     * @return a [Response] with the HTTP status and body
     * @throws Exception if there is an issue creating or executing the GET request
     */
    @Throws(Exception::class)
    fun getAllSavedOrders(accountNum: String, accessToken: String): Response {

        val url: String = API_URL + ACCOUNTS + SLASH + accountNum + SLASH + ORDERS
        val headerMap: Map<String, String> = mapOf(
            AUTHORIZATION to BEARER + accessToken
        )
        val request = createGetRequest(url, headerMap)
        return httpClient.executeRequest(request)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Place a specific existing order. Unless the realOrder flag is explicitly overridden, this function is
     * set to create saved orders as a safety mechanism.
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/post/accounts/%7BaccountId%7D/orders-0">Place Order</a>
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/post/accounts/%7BaccountId%7D/savedorders-0">Create Saved Order</a>
     *
     * @param order       to place
     * @param accountNum  to place order in
     * @param accessToken to authenticate with
     * @param realOrder   true indicates a real working order, false indicates a saved order
     * @return a [Response] with the HTTP status and body
     * @throws Exception if there is an issue creating or executing the POST request
     */
    @Throws(Exception::class)
    fun placeOrder(order: Order, accountNum: String, accessToken: String, realOrder: Boolean = false): Response {
        val url: String =
            if (realOrder) API_URL + ACCOUNTS + SLASH + accountNum + SLASH + ORDERS
            else API_URL + ACCOUNTS + SLASH + accountNum + SLASH + SAVED_ORDERS

        val request = createOrderRequest(url, order, accessToken)
        return httpClient.executeRequest(request)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Cancel a specific existing order. Unless the realOrder flag is explicitly overridden, this function is
     * set to cancel saved orders as a safety mechanism.
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/delete/accounts/%7BaccountId%7D/orders/%7BorderId%7D-0">Cancel Order</a>
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/delete/accounts/%7BaccountId%7D/savedorders/%7BsavedOrderId%7D-0">Delete Saved Order</a>
     *
     * @param accountNum  to cancel order in
     * @param accessToken to authenticate with
     * @param orderId     of order to cancel
     * @param realOrder   true indicates a real working order, false indicates a saved order
     * @return a [Response] with the HTTP status and body
     * @throws Exception if there is an issue creating or executing the DELETE request
     */
    fun cancelOrder(accountNum: String, accessToken: String, orderId: String, realOrder: Boolean = false): Response {
        val url: String =
            if (realOrder) API_URL + ACCOUNTS + SLASH + accountNum + SLASH + ORDERS + SLASH + orderId
            else API_URL + ACCOUNTS + SLASH + accountNum + SLASH + SAVED_ORDERS + SLASH + orderId

        val headerMap: Map<String, String> = mapOf(
            AUTHORIZATION to BEARER + accessToken
        )
        val request = createDeleteRequest(url, headerMap)
        return httpClient.executeRequest(request)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Replace a specific existing order. Unless the realOrder flag is explicitly overridden, this function is
     * set to replace saved orders as a safety mechanism.
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/put/accounts/%7BaccountId%7D/orders/%7BorderId%7D-0">Replace Order</a>
     * @see <a href="https://developer.tdameritrade.com/account-access/apis/put/accounts/%7BaccountId%7D/savedorders/%7BsavedOrderId%7D-0">Replace Saved Order</a>
     *
     * @param order       new order to replace with
     * @param accountNum  to replace order in
     * @param accessToken to authenticate with
     * @param orderId     of old order to replace
     * @param realOrder   true indicates a real working order, false indicates a saved order
     * @return a [Response] with the HTTP status and body
     * @throws Exception if there is an issue creating or executing the PUT request
     */
    fun replaceOrder(
        order: Order,
        accountNum: String,
        accessToken: String,
        orderId: String,
        realOrder: Boolean = false
    ): Response {
        val url: String =
            if (realOrder) API_URL + ACCOUNTS + SLASH + accountNum + SLASH + ORDERS + SLASH + orderId
            else API_URL + ACCOUNTS + SLASH + accountNum + SLASH + SAVED_ORDERS + SLASH + orderId

        val request = createOrderRequest(url, order, accessToken, true)
        return httpClient.executeRequest(request)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Translate an order to JSON format and create a POST/PUT request for order placement or replacement
     */
    private fun createOrderRequest(url: String, order: Order, accessToken: String, update: Boolean = false): Request? {
        val orderJson = mapper.writeValueAsString(order)
        val headerMap: Map<String, String> = mapOf(
            AUTHORIZATION to BEARER + accessToken,
            CONTENT_TYPE to APPLICATION_JSON
        )
        if (update) return createPutRequest(url, orderJson, headerMap)
        return createPostRequest(url, orderJson, headerMap)
    }
}
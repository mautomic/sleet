package com.sleet.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.sleet.api.util.RequestUtil.Companion.createGetRequest
import com.sleet.api.Constants
import com.sleet.api.Constants.DEFAULT_TIMEOUT_MILLIS
import com.sleet.api.model.Asset
import com.sleet.api.model.Contract
import com.sleet.api.model.OptionChain
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Response
import org.slf4j.LoggerFactory

import kotlin.Throws
import kotlin.system.exitProcess
import java.lang.Exception
import java.lang.StringBuilder
import java.util.ArrayList
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * An API interface that provides methods to retrieve option and equity data from the TD API
 *
 * @author mautomic
 */
class QuoteService(
    private val apiKey: String,
    private val httpClient: AsyncHttpClient
) {

    private var OPTION_CHAIN_URL: String = Constants.API_URL + Constants.MARKETDATA + "/chains?apikey=" + apiKey
    private var QUOTE_URL: String = "/quotes?apikey=$apiKey"

    companion object {
        private val LOG = LoggerFactory.getLogger(QuoteService::class.java)
        private val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    }

    /**
     * Queries the TD API endpoint for current quote info for a ticker
     *
     * @param ticker to get quote info for
     * @return an [Asset] with quote information
     */
    @Throws(Exception::class)
    fun getQuote(ticker: String): Asset? {
        return getQuoteAsync(ticker)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the TD API endpoint asynchronously for current quote info for a ticker
     *
     * @param ticker to get quote info for
     * @return an [Asset] with quote information
     */
    fun getQuoteAsync(ticker: String): CompletableFuture<Asset?> {
        val url = Constants.API_URL + Constants.MARKETDATA + "/" + ticker + QUOTE_URL
        val equityFuture = CompletableFuture<Asset?>()
        val request = createGetRequest(url, null)

        httpClient.executeRequest(request).toCompletableFuture().whenComplete { response: Response, _: Throwable? ->
            if (response.statusCode == 200) {
                try {
                    val node = mapper.readValue(response.responseBody, JsonNode::class.java)
                    equityFuture.complete(mapper.readValue(node[ticker].toString(), Asset::class.java))
                } catch (e: IOException) {
                    equityFuture.completeExceptionally(e)
                }
            } else
                equityFuture.complete(null)
        }
        return equityFuture
    }

    /**
     * Queries the TD API endpoint for current quote info for multiple tickers
     *
     * @param tickers to get quotes for
     * @return a list of [Asset] objects with quote information
     */
    @Throws(Exception::class)
    fun getQuotes(tickers: List<String?>): List<Asset> {
        val concatenated = java.lang.String.join("%2C", tickers)
        val url = Constants.API_URL + Constants.MARKETDATA + QUOTE_URL + Constants.QUERY_PARAM_SYMBOL + concatenated

        val request = createGetRequest(url, null)
        val responseFuture = httpClient.executeRequest(request).toCompletableFuture()
        val response = responseFuture[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
        if (response.statusCode != 200) {
            throw Exception("Error getting proper response from TD API: " + response.responseBody)
        }

        val json = response.responseBody
        val node = mapper.readValue(json, JsonNode::class.java)
        val equities: MutableList<Asset> = ArrayList(tickers.size)
        for (ticker in tickers) {
            val topLevel = node.path(ticker)
            val equity = mapper.treeToValue(topLevel, Asset::class.java)
            equities.add(equity)
        }
        return equities
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the default
     * number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getOptionChain(ticker: String?): OptionChain {
        return getOptionChain(ticker, Constants.DEFAULT_STRIKE_COUNT)
    }

    /**
     * Queries the TD API endpoint asynchronously for a ticker's option chain, getting
     * the default number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getOptionChainAsync(ticker: String?): CompletableFuture<OptionChain?> {
        return getOptionChainAsync(ticker, Constants.DEFAULT_STRIKE_COUNT)
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the specified
     * number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @param strikeCount of options to get in a single expiration period
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getOptionChain(ticker: String?, strikeCount: String?): OptionChain {
        val urls: MutableList<String> = ArrayList(2)
        val builder = StringBuilder()
            .append(OPTION_CHAIN_URL)
            .append(Constants.QUERY_PARAM_SYMBOL)
            .append(ticker)
            .append(Constants.QUERY_PARAM_STRIKE_COUNT)
            .append(strikeCount)
            .append(Constants.QUERY_PARAM_CONTRACT_TYPE)

        for (contract in Contract.values())
            urls.add(builder.toString() + contract.name)
        return getCallsAndPutsConcurrently(urls)
    }

    /**
     * Queries the TD API endpoint asynchronously for a ticker's option chain, getting
     * the specified number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @param strikeCount of options to get in a single expiration period
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getOptionChainAsync(ticker: String?, strikeCount: String?): CompletableFuture<OptionChain?> {
        val builder = StringBuilder()
            .append(OPTION_CHAIN_URL)
            .append(Constants.QUERY_PARAM_SYMBOL)
            .append(ticker)
            .append(Constants.QUERY_PARAM_STRIKE_COUNT)
            .append(strikeCount)

        val future = CompletableFuture<OptionChain?>()
        val request = createGetRequest(builder.toString(), null)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, filtering for contracts
     * expiring before a specified date
     *
     * @param ticker                 of security to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today, must follow the format of yyyy-MM-dd
     * @param otmOnly                if only out-of-the-money options should be returned, or all
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getCloseExpirationOptionChain(ticker: String?, furthestExpirationDate: String?, otmOnly: Boolean): OptionChain {
        return getCloseExpirationOptionChain(ticker, furthestExpirationDate, Constants.DEFAULT_STRIKE_COUNT, otmOnly)
    }

    /**
     * Queries the TD API endpoint asynchronously for a ticker's option chain, filtering
     * for contracts expiring before a specified date
     *
     * @param ticker                 of security to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today, must follow the format of yyyy-MM-dd
     * @param otmOnly                if only out-of-the-money options should be returned, or all
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getCloseExpirationOptionChainAsync(
        ticker: String?, furthestExpirationDate: String?,
        otmOnly: Boolean
    ): CompletableFuture<OptionChain?> {
        return getCloseExpirationOptionChainAsync(
            ticker,
            furthestExpirationDate,
            Constants.DEFAULT_STRIKE_COUNT,
            otmOnly
        )
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, filtering for contracts
     * expiring before a specified date and the specified number of strikes
     *
     * @param ticker                 of security to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today, must follow the format of yyyy-MM-dd
     * @param strikeCount            of options to get in a single expiration period
     * @param otmOnly                if only out-of-the-money options should be returned, or all
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getCloseExpirationOptionChain(
        ticker: String?, furthestExpirationDate: String?, strikeCount: String?,
        otmOnly: Boolean
    ): OptionChain {
        val urls: MutableList<String> = ArrayList(2)
        val builder = StringBuilder()
            .append(OPTION_CHAIN_URL)
            .append(Constants.QUERY_PARAM_SYMBOL)
            .append(ticker)
            .append(Constants.QUERY_PARAM_STRIKE_COUNT)
            .append(strikeCount)
            .append(Constants.QUERY_PARAM_TO_DATE)
            .append(furthestExpirationDate)

        if (otmOnly)
            builder.append(Constants.QUERY_PARAM_OTM)
        for (contract in Contract.values())
            urls.add(builder.toString() + contract.name)
        return getCallsAndPutsConcurrently(urls)
    }

    /**
     * Queries the TD API endpoint asynchronously for a ticker's option chain, filtering
     * for contracts expiring before a specified date and the specified number of strikes
     *
     * @param ticker                 of security to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today, must follow the format of yyyy-MM-dd
     * @param strikeCount            of options to get in a single expiration period
     * @param otmOnly                if only out-of-the-money options should be returned, or all
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getCloseExpirationOptionChainAsync(
        ticker: String?, furthestExpirationDate: String?, strikeCount: String?,
        otmOnly: Boolean
    ): CompletableFuture<OptionChain?> {
        val builder = StringBuilder()
            .append(OPTION_CHAIN_URL)
            .append(Constants.QUERY_PARAM_SYMBOL)
            .append(ticker)
            .append(Constants.QUERY_PARAM_STRIKE_COUNT)
            .append(strikeCount)
            .append(Constants.QUERY_PARAM_TO_DATE)
            .append(furthestExpirationDate)
        if (otmOnly)
            builder.append(Constants.QUERY_PARAM_OTM)

        val future = CompletableFuture<OptionChain?>()
        val request = createGetRequest(builder.toString(), null)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the TD API endpoint for all options for a ticker on a specific expiration date
     *
     * @param ticker         of security to retrieve options for
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getOptionChainForDate(ticker: String?, expirationDate: String?): OptionChain? {
        return getOptionChainForDateAsync(ticker, expirationDate)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the TD API endpoint asynchronously for all options for a ticker on a specific expiration date
     *
     * @param ticker of security to retrieve options for
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getOptionChainForDateAsync(ticker: String?, expirationDate: String?): CompletableFuture<OptionChain?> {
        val builder = StringBuilder()
            .append(OPTION_CHAIN_URL)
            .append(Constants.QUERY_PARAM_SYMBOL)
            .append(ticker)
            .append(Constants.QUERY_PARAM_STRIKE_COUNT)
            .append(Constants.DEFAULT_STRIKE_COUNT)
            .append(Constants.QUERY_PARAM_TO_DATE)
            .append(expirationDate)
            .append(Constants.QUERY_PARAM_FROM_DATE)
            .append(expirationDate)

        val future = CompletableFuture<OptionChain?>()
        val request = createGetRequest(builder.toString(), null)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the TD API endpoint for all options for a ticker with a specific strike price
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getOptionChainForStrike(ticker: String?, strike: String?): OptionChain? {
        return getOptionChainForStrikeAsync(ticker, strike)[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the TD API endpoint asynchronously for all options for a ticker with a specific strike price
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getOptionChainForStrikeAsync(ticker: String?, strike: String?): CompletableFuture<OptionChain?> {
        val builder = StringBuilder()
            .append(OPTION_CHAIN_URL)
            .append(Constants.QUERY_PARAM_SYMBOL)
            .append(ticker)
            .append(Constants.QUERY_PARAM_STRIKE)
            .append(strike)

        val future = CompletableFuture<OptionChain?>()
        val request = createGetRequest(builder.toString(), null)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the TD API endpoint for options for a ticker on a specific expiration
     * date with a specific strike
     *
     * @param ticker         of security to retrieve options for
     * @param strike         of the options to retrieve
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getOptionChainForStrikeAndDate(ticker: String?, strike: String?, expirationDate: String?): OptionChain? {
        val future = getOptionChainForStrikeAndDateAsync(ticker, strike, expirationDate)
        return future[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the TD API endpoint asynchronously for options for a ticker on a
     * specific expiration date with a specific strike
     *
     * @param ticker         of security to retrieve options for
     * @param strike         of the options to retrieve
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getOptionChainForStrikeAndDateAsync(
        ticker: String?, strike: String?,
        expirationDate: String?
    ): CompletableFuture<OptionChain?> {
        val builder = StringBuilder()
            .append(OPTION_CHAIN_URL)
            .append(Constants.QUERY_PARAM_SYMBOL)
            .append(ticker)
            .append(Constants.QUERY_PARAM_TO_DATE)
            .append(expirationDate)
            .append(Constants.QUERY_PARAM_FROM_DATE)
            .append(expirationDate)
            .append(Constants.QUERY_PARAM_STRIKE)
            .append(strike)

        val future = CompletableFuture<OptionChain?>()
        val request = createGetRequest(builder.toString(), null)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Method to run two [OptionChain] requests concurrently, one just for calls, and one just for puts.
     * As the bottleneck for presenting large quantities of [Option]s back to the caller is the request
     * to TD, this effectively cuts the retrieval speed in half. [OptionChain]s are returned in an async
     * fashion via a [CompletableFuture].
     *
     * @param urls to send GET requests
     * @return [OptionChain] for the original request
     */
    @Throws(Exception::class)
    private fun getCallsAndPutsConcurrently(urls: List<String>): OptionChain {
        val futures: List<CompletableFuture<OptionChain?>> = listOf(CompletableFuture(), CompletableFuture())

        for ((index, url) in urls.withIndex()) {
            val future = futures[index]
            val request = createGetRequest(url, null)
            httpClient.executeRequest(request).toCompletableFuture()
                .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        }
        CompletableFuture.allOf(*futures.toTypedArray<CompletableFuture<*>>())[DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]

        // Combine the two chains
        val fullChain = futures[0].get()
        if (fullChain!!.callExpDateMap!!.isEmpty())
            fullChain.callExpDateMap = futures[1].get()!!.callExpDateMap
        else
            fullChain.putExpDateMap = futures[1].get()!!.putExpDateMap
        return fullChain
    }

    /**
     * Deserialize a JSON response string into an [OptionChain]
     *
     * @param response to deserialize
     * @return [OptionChain] for the original request, or null if exception occurs
     */
    private fun deserializeResponse(response: Response): OptionChain? {
        if (response.statusCode == 200) {
            try {
                return mapper.readValue(response.responseBody, OptionChain::class.java)
            } catch (e: Exception) {
                logFailure(e)
            }
        }
        return null
    }

    /**
     * Method for logging exceptions after failed HTTP requests. Exit program
     * if api key expires.
     */
    private fun logFailure(e: Exception) {
        LOG.error("Could not retrieve option chain", e)
        if (e.message!!.contains("InvalidApiKey"))
            exitProcess(1)
    }
}
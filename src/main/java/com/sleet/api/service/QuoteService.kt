package com.sleet.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.sleet.api.util.RequestUtil.Companion.createGetRequest
import com.sleet.api.Constants
import com.sleet.api.model.*
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Response
import org.slf4j.LoggerFactory

import kotlin.Throws
import kotlin.system.exitProcess
import java.lang.StringBuilder
import java.util.ArrayList
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * An API interface that provides methods to retrieve option and equity data from the Schwab API
 *
 * @author mautomic
 */
class QuoteService(
    private var apiKey: String,
    private val httpClient: AsyncHttpClient
) {
    private var MARKETDATA_URL: String = Constants.API_URL + "marketdata/v1/"
    private var OPTION_CHAIN_URL: String = MARKETDATA_URL + "chains?"
    private var HIST_PRICE_URL: String = MARKETDATA_URL + "pricehistory?"
    private var QUOTES_URL: String = MARKETDATA_URL + "quotes?"
    private var MOVERS_URL: String = MARKETDATA_URL + "/movers/"
    private val MOVERS_LIST: List<String> = listOf(
        "\$SPX", "\$COMPX", "\$DJI", "NYSE", "NASDAQ", "OTCBB", "INDEX_ALL", "EQUITY_ALL",
        "OPTION_ALL", "OPTION_PUT", "OPTION_CALL"
    )
    private val MOVERS_SORT: List<String> = listOf("VOLUME", "TRADES", "PERCENT_CHANGE_UP", "PERCENT_CHANGE_DOWN")
    private val MOVERS_FREQUENCY: List<String> = listOf("0", "1", "5", "10", "30", "60")

    companion object {
        private val LOG = LoggerFactory.getLogger(QuoteService::class.java)
        private val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    }

    fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    /**
     * Queries the Schwab API endpoint for current quote info for a ticker
     *
     * @param ticker to get quote info for
     * @return an [Equity] with quote information
     */
    @Throws(Exception::class)
    fun getQuote(ticker: String): Equity? {
        return getQuoteAsync(ticker)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the Schwab API endpoint asynchronously for current quote info for a ticker
     *
     * @param ticker to get quote info for
     * @return an [Equity] with quote information
     */
    fun getQuoteAsync(ticker: String): CompletableFuture<Equity?> {
        val url = "$MARKETDATA_URL$ticker/quotes?fields=quote"
        val equityFuture = CompletableFuture<Equity?>()
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(url, headers)


        httpClient.executeRequest(request).toCompletableFuture().whenComplete { response: Response, _: Throwable? ->
            if (response.statusCode == 200) {
                try {
                    val node = mapper.readValue(response.responseBody, JsonNode::class.java)
                    equityFuture.complete(mapper.readValue(node[ticker].toString(), Equity::class.java))
                } catch (e: IOException) {
                    equityFuture.completeExceptionally(e)
                }
            } else
                equityFuture.complete(null)
        }
        return equityFuture
    }

    /**
     * Queries the Schwab API endpoint for current quote info for multiple tickers
     *
     * @param tickers to get quotes for
     * @return a list of [Equity] objects with quote information
     */
    @Throws(Exception::class)
    fun getQuotes(tickers: List<String?>): List<Equity> {
        val concatenated = java.lang.String.join("%2C", tickers)
        val url = QUOTES_URL + "symbols=" + concatenated

        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(url, headers)
        val responseFuture = httpClient.executeRequest(request).toCompletableFuture()
        val response = responseFuture[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
        if (response.statusCode != 200) {
            throw Exception("Error getting proper response from Schwab API: " + response.responseBody)
        }

        val json = response.responseBody
        val node = mapper.readValue(json, JsonNode::class.java)
        val equities: MutableList<Equity> = ArrayList(tickers.size)
        for (ticker in tickers) {
            val topLevel = node.path(ticker)
            val equity = mapper.treeToValue(topLevel, Equity::class.java)
            equities.add(equity)
        }
        return equities
    }

    /**
     * Queries the Schwab API endpoint for movers of an index
     *
     * @param symbol of security to retrieve movers for
     * @return [Screener] with all candidates
     */
    @Throws(Exception::class)
    fun getMovers(symbol: String, sort: String, frequency: String): Screener? {
        return getMoversAsync(symbol, sort, frequency)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the Schwab API endpoint for movers of an index
     *
     * @param symbol of security to retrieve movers for
     * @return [Screener] with all candidates
     */
    @Throws(Exception::class)
    fun getMoversAsync(symbol: String, sort: String, frequency: String): CompletableFuture<Screener?> {
        val builder = StringBuilder()
            .append(MOVERS_URL)
            .append(symbol)
            .append(Constants.QUESTION_MARK)
            .append(Constants.QUERY_PARAM_SORT)
            .append(sort)
            .append(Constants.QUERY_PARAM_FREQUENCY)
            .append(frequency)

        val screenersFuture = CompletableFuture<Screener?>()
        if (!MOVERS_LIST.contains(symbol)) {
            return screenersFuture
        }
        if (!MOVERS_SORT.contains(sort)) {
            return screenersFuture
        }
        if (!MOVERS_FREQUENCY.contains(frequency)) {
            return screenersFuture
        }

        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(builder.toString(), headers)

        httpClient.executeRequest(request).toCompletableFuture().whenComplete { response: Response, _: Throwable? ->
            if (response.statusCode == 200) {
                try {
                    val node = mapper.readValue(response.responseBody, JsonNode::class.java)
                    screenersFuture.complete(mapper.readValue(node.toString(), Screener::class.java))
                } catch (e: IOException) {
                    screenersFuture.completeExceptionally(e)
                }
            } else
                screenersFuture.complete(null)
        }
        return screenersFuture
    }


    /**
     * Queries the Schwab API endpoint for a ticker's option chain, getting the default
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
     * Queries the Schwab API endpoint asynchronously for a ticker's option chain, getting
     * the default number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @return [CompletableFuture] with an [OptionChain]
     */
    fun getOptionChainAsync(ticker: String?): CompletableFuture<OptionChain?> {
        return getOptionChainAsync(ticker, Constants.DEFAULT_STRIKE_COUNT)
    }

    /**
     * Queries the Schwab API endpoint for a ticker's option chain, getting the specified
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
     * Queries the Schwab API endpoint asynchronously for a ticker's option chain, getting
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
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(builder.toString(), headers)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the Schwab API endpoint for a ticker's option chain, filtering for contracts
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
     * Queries the Schwab API endpoint asynchronously for a ticker's option chain, filtering
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
     * Queries the Schwab API endpoint for a ticker's option chain, filtering for contracts
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
     * Queries the Schwab API endpoint asynchronously for a ticker's option chain, filtering
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
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(builder.toString(), headers)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the Schwab API endpoint for all options for a ticker on a specific expiration date
     *
     * @param ticker         of security to retrieve options for
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getOptionChainForDate(ticker: String?, expirationDate: String?): OptionChain? {
        return getOptionChainForDateAsync(ticker, expirationDate)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the Schwab API endpoint asynchronously for all options for a ticker on a specific expiration date
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
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(builder.toString(), headers)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the Schwab API endpoint for all options for a ticker with a specific strike price
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @return [OptionChain] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getOptionChainForStrike(ticker: String?, strike: String?): OptionChain? {
        return getOptionChainForStrikeAsync(ticker, strike)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the Schwab API endpoint for historical prices for a specified period & frequency
     *
     * @param ticker of security to retrieve prices for
     * @param periodType type of period to show, e.g. [day, month, year, ytd]
     * @param period number of periods to show
     * @param frequencyType type of frequency with which a new candle is formed, e.g. [minute, daily, weekly, monthly]
     * @param frequency the number of frequencies to be included
     * @param startDate start date as milliseconds since epoch
     * @param endDate end date as milliseconds since epoch
     * @return [Response] with all option data for the ticker
     */
    @Throws(Exception::class)
    fun getPriceHistory(ticker: String?, periodType: String?, period: String?, frequencyType: String?, frequency: String?,
                        startDate: String?, endDate: String?): CompletableFuture<Candles?> {

        val builder = StringBuilder()
            .append(HIST_PRICE_URL)
            .append("symbol=")
            .append(ticker)
            .append(Constants.QUERY_PARAM_PERIOD_TYPE)
            .append(periodType)
            .append(Constants.QUERY_PARAM_PERIOD)
            .append(period)
            .append(Constants.QUERY_PARAM_FREQUENCY_TYPE)
            .append(frequencyType)
            .append(Constants.QUERY_PARAM_FREQUENCY)
            .append(frequency)

        val future = CompletableFuture<Candles?>()
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(builder.toString(), headers)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeHistoryResponse(resp)) }
        return future
    }

    /**
     * Queries the Schwab API endpoint asynchronously for all options for a ticker with a specific strike price
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
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(builder.toString(), headers)
        httpClient.executeRequest(request).toCompletableFuture()
            .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        return future
    }

    /**
     * Queries the Schwab API endpoint for options for a ticker on a specific expiration
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
        return future[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
    }

    /**
     * Queries the Schwab API endpoint asynchronously for options for a ticker on a
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
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )
        val request = createGetRequest(builder.toString(), headers)
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
        val headers = mapOf(
            "Authorization" to "Bearer $apiKey"
        )

        for ((index, url) in urls.withIndex()) {
            val future = futures[index]
            val request = createGetRequest(url, headers)
            httpClient.executeRequest(request).toCompletableFuture()
                .whenComplete { resp: Response, _: Throwable? -> future.complete(deserializeResponse(resp)) }
        }
        CompletableFuture.allOf(*futures.toTypedArray<CompletableFuture<*>>())[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]

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
     * Deserialize a JSON response string into an [PriceHistory]
     *
     * @param response to deserialize
     * @return [PriceHistory] for the original request, or null if exception occurs
     */
    private fun deserializeHistoryResponse(response: Response): Candles? {
        if (response.statusCode == 200) {
            try {
                return mapper.readValue(response.responseBody, Candles::class.java)
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
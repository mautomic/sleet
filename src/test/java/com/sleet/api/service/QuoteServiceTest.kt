package com.sleet.api.service

import com.sleet.api.model.OptionChain
import com.sleet.api.model.Asset
import org.asynchttpclient.Dsl
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import kotlin.Throws
import java.lang.Exception
import java.util.ArrayList
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.Arrays

/**
 * Test class for [QuoteService]
 *
 * @author mautomic
 */
class QuoteServiceTest {

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testOptionChainRequest() {
        val quoteService = QuoteService(TestConstants.API_KEY, Dsl.asyncHttpClient(Dsl.config()))
        val time = System.currentTimeMillis()
        val optionChain = quoteService.getOptionChain("SPY", "50")
        println("Retrieval for SPY options took " + (System.currentTimeMillis() - time) + " ms")

        Assert.assertNotNull(optionChain)
        Assert.assertNotNull(optionChain.callExpDateMap)
        Assert.assertNotNull(optionChain.putExpDateMap)
        Assert.assertEquals("SPY", optionChain.symbol)
        Assert.assertFalse(optionChain.callExpDateMap!!.isEmpty())
        Assert.assertFalse(optionChain.putExpDateMap!!.isEmpty())
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testOptionChainRequestForStrikeAndDate() {
        val quoteService = QuoteService(TestConstants.API_KEY, Dsl.asyncHttpClient(Dsl.config()))
        val optionChain = quoteService.getOptionChainForStrikeAndDate("SPY", "400", "2023-01-20")
        Assert.assertNotNull(optionChain)
        Assert.assertNotNull(optionChain!!.callExpDateMap)
        Assert.assertNotNull(optionChain.putExpDateMap)

        val map = optionChain.callExpDateMap
        val strikes = map?.entries?.iterator()?.next()?.value
        val asset = strikes!!["400.0"]!![0]
        Assert.assertEquals("SPY_012023C400", asset.symbol)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testContinuousOptionScanningPerformance() {
        val quoteService = QuoteService(TestConstants.API_KEY, Dsl.asyncHttpClient(Dsl.config()))
        val tickers = arrayOf(
            "QQQ", "SPY", "IWM", "\$VIX.X", "\$SPX.X", "MSFT", "AAPL", "NFLX", "FB", "TSLA",
            "NVDA", "BYND", "TLT", "SPCE", "XLF"
        )
        val startTime = System.currentTimeMillis()
        for (j in 0..2) {
            val futures: MutableList<CompletableFuture<OptionChain?>> = ArrayList()
            val time = System.currentTimeMillis()
            for (ticker in tickers)
                futures.add(quoteService.getOptionChainAsync(ticker, "100"))

            CompletableFuture.allOf(*futures.toTypedArray<CompletableFuture<*>>())[20, TimeUnit.SECONDS]
            println("Retrieval for " + Arrays.toString(tickers) + "  took " + (System.currentTimeMillis() - time) + " ms")
        }
        println("Took total of " + (System.currentTimeMillis() - startTime) + " ms")
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testQuoteRequest() {
        val quoteService = QuoteService(TestConstants.API_KEY, Dsl.asyncHttpClient(Dsl.config()))
        val time = System.currentTimeMillis()
        val equity = quoteService.getQuote("SPY")
        println("Retrieval for SPY quote info took " + (System.currentTimeMillis() - time) + " ms")

        val time2 = System.currentTimeMillis()
        val equity2 = quoteService.getQuote("AAPL")
        println("Retrieval for AAPL quote info took " + (System.currentTimeMillis() - time2) + " ms")

        Assert.assertNotNull(equity)
        Assert.assertEquals(218.26, equity!!.fiftyTwoWeekLow, 0.0001)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testMultipleTickers() {
        val quoteService = QuoteService(TestConstants.API_KEY, Dsl.asyncHttpClient(Dsl.config()))
        val tickers = Arrays.asList("SPY", "AAPL", "MSFT")
        val time = System.currentTimeMillis()
        val equities = quoteService.getQuotes(tickers)
        println("Retrieval for multiple quotes info took " + (System.currentTimeMillis() - time) + " ms")

        Assert.assertNotNull(equities)
        Assert.assertFalse(equities.isEmpty())
        Assert.assertEquals(3, equities.size.toLong())
        Assert.assertEquals(1, equities.stream()
            .filter { equity: Asset -> equity.symbol!!.contains("SPY") }
            .count())
        Assert.assertEquals(218.26, equities.stream()
            .filter { equity: Asset -> equity.symbol!!.contains("SPY") }
            .mapToDouble(Asset::fiftyTwoWeekLow)
            .sum(), 0.0001)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testContinuousQuoteScanningPerformance() {
        val quoteService = QuoteService(TestConstants.API_KEY, Dsl.asyncHttpClient(Dsl.config()))
        val tickers = arrayOf("QQQ", "DIS", "AAPL", "FB", "SPY", "MSFT", "\$VIX.X", "AMD", "AMZN", "\$SPX.X")
        for (j in 0..2) {
            for (ticker in tickers) {
                val time = System.currentTimeMillis()
                quoteService.getQuote(ticker)
                println("Retrieval for " + ticker + " quote info took " + (System.currentTimeMillis() - time) + " ms")
            }
            // Throttle so TD API doesn't hit max requests per second limit
            Thread.sleep(2000)
        }
    }
}
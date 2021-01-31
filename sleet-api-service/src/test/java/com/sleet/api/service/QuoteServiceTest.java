package com.sleet.api.service;

import com.sleet.api.model.Equity;
import com.sleet.api.model.Option;
import com.sleet.api.model.OptionChain;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test class for {@link QuoteService}
 *
 * @author mautomic
 */
public class QuoteServiceTest {

    @Test
    public void testOptionChainRequest() throws Exception {
        final QuoteService quoteService = new QuoteService(TestConstants.API_KEY);

        long time = System.currentTimeMillis();
        final OptionChain optionChain = quoteService.getOptionChain("SPY", "50");
        System.out.println("Retrieval for SPY options took " + (System.currentTimeMillis() - time) + " ms");

        Assert.assertNotNull(optionChain);
        Assert.assertNotNull(optionChain.getCallExpDateMap());
        Assert.assertNotNull(optionChain.getPutExpDateMap());

        Assert.assertEquals("SPY", optionChain.getSymbol());

        Assert.assertFalse(optionChain.getCallExpDateMap().isEmpty());
        Assert.assertFalse(optionChain.getPutExpDateMap().isEmpty());
    }

    @Test
    public void testOptionChainRequestForStrikeAndDate() throws Exception {

        final QuoteService quoteService = new QuoteService(TestConstants.API_KEY);
        final OptionChain optionChain = quoteService.getOptionChainForStrikeAndDate("TLT", "155.5", "2020-12-18");

        Assert.assertNotNull(optionChain);
        Assert.assertNotNull(optionChain.getCallExpDateMap());
        Assert.assertNotNull(optionChain.getPutExpDateMap());

        Map<String, Map<String, List<Option>>> map = optionChain.getCallExpDateMap();
        Option option = map.get("2020-12-18:14").get("155.5").get(0);
        Assert.assertEquals("TLT_121820C155.5", option.getSymbol());
    }

    @Test
    public void testContinuousOptionScanningPerformance() throws Exception {
        final QuoteService quoteService = new QuoteService(TestConstants.API_KEY);

        final String[] tickers = {"QQQ", "SPY", "IWM", "$VIX.X", "$SPX.X", "MSFT", "AAPL", "NFLX", "FB", "TSLA",
                "NVDA", "BYND", "TLT", "SPCE", "XLF"};

        final long startTime = System.currentTimeMillis();
        for (int j = 0; j < 3; j++) {

            List<CompletableFuture<OptionChain>> futures = new ArrayList<>();
            long time = System.currentTimeMillis();
            for (String ticker : tickers) {
                futures.add(quoteService.getOptionChainAsync(ticker, "100"));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(20, TimeUnit.SECONDS);
            System.out.println("Retrieval for " + Arrays.toString(tickers) + "  took " + (System.currentTimeMillis() - time) + " ms");
        }
        System.out.println("Took total of " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Test
    public void testQuoteRequest() throws Exception {
        final QuoteService quoteService = new QuoteService(TestConstants.API_KEY);

        long time = System.currentTimeMillis();
        Equity equity = quoteService.getQuote("SPY");
        System.out.println("Retrieval for SPY quote info took " + (System.currentTimeMillis() - time) + " ms");

        long time2 = System.currentTimeMillis();
        Equity equity2 = quoteService.getQuote("AAPL");
        System.out.println("Retrieval for AAPL quote info took " + (System.currentTimeMillis() - time2) + " ms");

        Assert.assertNotNull(equity);
        Assert.assertEquals(218.26, equity.getFiftyTwoWeekLow(), 0.0001);
    }

    @Test
    public void testMultipleTickers() throws Exception {
        final QuoteService quoteService = new QuoteService(TestConstants.API_KEY);

        List<String> tickers = Arrays.asList("SPY", "AAPL", "MSFT");
        long time = System.currentTimeMillis();
        final List<Equity> equities = quoteService.getQuotes(tickers);
        System.out.println("Retrieval for multiple quotes info took " + (System.currentTimeMillis() - time) + " ms");

        Assert.assertNotNull(equities);
        Assert.assertFalse(equities.isEmpty());
        Assert.assertEquals(3, equities.size());
        Assert.assertEquals(1, equities.stream()
                .filter(equity -> equity.getSymbol().contains("SPY"))
                .count());
        Assert.assertEquals(218.26, equities.stream()
                .filter(equity -> equity.getSymbol().contains("SPY"))
                .mapToDouble(Equity::getFiftyTwoWeekLow)
                .sum(), 0.0001);
    }

    @Test
    public void testContinuousQuoteScanningPerformance() throws Exception {
        final QuoteService quoteService = new QuoteService(TestConstants.API_KEY);

        final String[] tickers = {"QQQ", "DIS", "AAPL", "FB", "SPY", "MSFT", "$VIX.X", "AMD", "AMZN", "$SPX.X"};
        for (int j=0; j<3; j++) {
            for (String ticker : tickers) {
                long time = System.currentTimeMillis();
                quoteService.getQuote(ticker);
                System.out.println("Retrieval for " + ticker + " quote info took " + (System.currentTimeMillis() - time) + " ms");
            }
            // Throttle so TD API doesn't hit max requests per second limit
            Thread.sleep(2000);
        }
    }
}

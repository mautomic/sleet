package com.sleet.api.service;

import com.sleet.api.model.Equity;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Test class for {@link QuoteService}
 *
 * @author mautomic
 */
public class QuoteServiceTest {

    @Test
    public void testQuoteRequest() throws Exception {
        final QuoteService quoteService = new QuoteService(TestConstants.API_KEY);

        long time = System.currentTimeMillis();
        final Optional<Equity> equityOptional = quoteService.getQuote("SPY");
        System.out.println("Retrieval for SPY quote info took " + (System.currentTimeMillis() - time) + " ms");

        long time2 = System.currentTimeMillis();
        final Optional<Equity> equity2Optional = quoteService.getQuote("AAPL");
        System.out.println("Retrieval for AAPL quote info took " + (System.currentTimeMillis() - time2) + " ms");

        Assert.assertNotNull(equityOptional.filter(equity -> equity.getSymbol().contains("SPY")));
        Assert.assertEquals(218.26, equityOptional.get().getFiftyTwoWeekLow(), 0.0001);
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

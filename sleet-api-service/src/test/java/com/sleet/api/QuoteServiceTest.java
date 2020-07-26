package com.sleet.api;

import com.sleet.api.model.Equity;
import com.sleet.api.service.QuoteService;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link QuoteService}
 *
 * @author mautomic
 */
public class QuoteServiceTest {

    @Test
    public void testQuoteRequest() {

        // Must supply API key for TD API in order to run test. See readme for info.
        final String apiKey = "";
        final QuoteService quoteService = new QuoteService(apiKey);

        long time = System.currentTimeMillis();
        final Equity equity = quoteService.getQuote("SPY");
        System.out.println("Retrieval for SPY quote info took " + (System.currentTimeMillis() - time) + " ms");

        long time2 = System.currentTimeMillis();
        final Equity equity2 = quoteService.getQuote("AAPL");
        System.out.println("Retrieval for AAPL quote info took " + (System.currentTimeMillis() - time2) + " ms");

        Assert.assertNotNull(equity);
        Assert.assertNotNull(equity.getSymbol());
        Assert.assertEquals("SPY", equity.getSymbol());
        Assert.assertEquals(218.26, equity.getFiftyTwoWeekLow(), 0.0001);
    }

    @Test
    public void testContinuousQuoteScanningPerformance() throws InterruptedException {

        // Must supply API key for TD API in order to run test. See readme for info.
        final String apiKey = "";
        final QuoteService quoteService = new QuoteService(apiKey);

        final String[] tickers = {"QQQ", "DIS", "AAPL", "FB", "SPY", "MSFT", "$VIX.X", "AMD", "AMZN", "$SPX.X"};
        for (int j=0; j<3; j++) {
            for (int i = 0; i < tickers.length; i++) {
                long time = System.currentTimeMillis();
                quoteService.getQuote(tickers[i]);
                System.out.println("Retrieval for " + tickers[i] + " quote info took " + (System.currentTimeMillis() - time) + " ms");
            }
            // Throttle so TD API doesn't hit max requests per second limit
            Thread.sleep(2000);
        }

    }
}

package com.sleet.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sleet.api.HttpClient;
import com.sleet.api.model.Equity;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Service} implementation that provides methods to retrieve equity quote data from the TD API
 *
 * @author mautomic
 */
public class QuoteService extends Service {

    private static String QUOTE_URL;
    private static final Logger LOG = LoggerFactory.getLogger(OptionService.class);

    public QuoteService(final String apiKey) {
        httpClient = new HttpClient(DEFAULT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS);
        QUOTE_URL = "/quotes?apikey=" + apiKey;
    }

    /**
     * Queries the TD API endpoint for current quote info for a ticker
     *
     * @param ticker to get quote info for
     * @return an {@link Equity} with quote information
     */
    public Equity getQuote(final String ticker) throws Exception {
        final String url = API_URL + ticker + QUOTE_URL;
        final CompletableFuture<Response> responseFuture = httpClient.get(url);
        final Response response = responseFuture.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        if (response.getStatusCode() == 200) {
            final String json = response.getResponseBody();
            final JsonNode node = mapper.readValue(json, JsonNode.class);
            return mapper.readValue(node.get(ticker).toString(), Equity.class);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint for current quote info for multiple tickers
     *
     * @param tickers to get quotes for
     * @return a list of {@link Equity} objects with quote information
     */
    public List<Equity> getQuotes(final List<String> tickers) {
        // TODO: Implement multiple quotes query
        return null;
    }
}

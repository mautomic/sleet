package com.sleet.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sleet.api.HttpClient;
import com.sleet.api.model.Equity;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Service} implementation that provides methods to retrieve equity quote data from the TD API
 *
 * @author mautomic
 */
public class QuoteService extends Service {

    private static String QUOTE_URL;

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
    public Optional<Equity> getQuote(final String ticker) throws Exception {
        final String url = API_URL + MARKETDATA + "/" + ticker + QUOTE_URL;
        final CompletableFuture<Response> responseFuture = httpClient.get(url, null);
        final Response response = responseFuture.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        if (response.getStatusCode() == 200) {
            final String json = response.getResponseBody();
            final JsonNode node = mapper.readValue(json, JsonNode.class);
            return Optional.ofNullable(mapper.readValue(node.get(ticker).toString(), Equity.class));
        }
        return Optional.empty();
    }

    /**
     * Queries the TD API endpoint for current quote info for multiple tickers
     *
     * @param tickers to get quotes for
     * @return a list of {@link Equity} objects with quote information
     */
    public List<Equity> getQuotes(final List<String> tickers) throws Exception {
        String concatenated = String.join("%2C", tickers);
        final String url = API_URL + QUOTE_URL + "&symbol=" + concatenated;
        final CompletableFuture<Response> responseFuture = httpClient.get(url, null);
        final Response response = responseFuture.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        if (response.getStatusCode() == 200) {
            final String json = response.getResponseBody();
            final JsonNode node = mapper.readValue(json, JsonNode.class);

            List<Equity> equities = new ArrayList<>(tickers.size());

            for (String ticker : tickers) {
                JsonNode topLevel = node.path(ticker);
                Equity equity = mapper.treeToValue(topLevel, Equity.class);
                equities.add(equity);
            }
            return equities;
        }
        return Collections.emptyList();
    }
}


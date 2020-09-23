package com.sleet.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleet.api.model.Equity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Service} implementation that provides methods to retrieve equity quote data from the TD API
 */
public class QuoteService extends Service {

    private static String QUOTE_URL;
    private static final ObjectMapper mapper = new ObjectMapper();

    public QuoteService(final String apiKey) {
        restTemplate = new RestTemplate(getClientHttpRequestFactory());
        QUOTE_URL = "/quotes?apikey=" + apiKey;
    }

    /**
     * Queries the TD API endpoint for current quote info for a ticker
     *
     * @param ticker to get quote info for
     * @return an {@link Equity} with quote information
     */
    public Optional<Equity> getQuote(final String ticker) throws Exception {

        final String url = API_URL + ticker + QUOTE_URL;
        final ResponseEntity<JsonNode> json = restTemplate.getForEntity(url, JsonNode.class);
        Equity equity = mapper.readValue(Objects.requireNonNull(json.getBody())
                .get(ticker).toString(), Equity.class);
        return Optional.ofNullable(equity);
    }

    /**
     * Queries the TD API endpoint for current quote info for multiple tickers
     *
     * @param tickers to get quote info for
     * @return an Optional List of {@link Equity} objects with quote information
     */
    public List<Equity> getQuotes(final String... tickers) throws Exception {

        String concatenated = String.join("%2C", tickers);
        final String url = API_URL + QUOTE_URL + "&symbol=" + concatenated;

        JsonNode node = mapper.readTree(new URL(url));
        List<Equity> equities = new ArrayList<>(tickers.length);

        for (String ticker : tickers) {
            JsonNode topLevel = node.path(ticker);
            Equity equity = mapper.treeToValue(topLevel, Equity.class);
            equities.add(equity);
        }
        return equities;
    }
}

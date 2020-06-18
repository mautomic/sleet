package com.sleet.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sleet.api.model.Equity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.*;

/**
 * A {@link Service} implementation that provides methods to retrieve equity quote data from the TD API
 */
public class QuoteService extends Service {

    private static String QUOTE_URL;
    private static final Logger LOG = LoggerFactory.getLogger(OptionService.class);
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
    public Optional<Equity> getQuote(final String ticker) {

        final String url = API_URL + ticker + QUOTE_URL;
        try {
            final ResponseEntity<JsonNode> json = restTemplate.getForEntity(url, JsonNode.class);
            Equity equity = mapper.readValue(Objects.requireNonNull(json.getBody())
                    .get(ticker).toString(), Equity.class);
            return Optional.ofNullable(equity);
        } catch (Exception e) {
            LOG.error("Could not retrieve quote for " + ticker, e);
        }
        return Optional.empty();
    }

    public Optional<List<Equity>> getQuotes(final String... tickers) {

        String concatenated = String.join("%2C", tickers);
        final String url = API_URL + QUOTE_URL + "&symbol=" + concatenated;

        try {
            JsonNode node = mapper.readTree(new URL(url));
            List<Equity> equities = new ArrayList<>(tickers.length);

            for (String ticker : tickers) {
                JsonNode topLevel = node.path(ticker);
                Equity equity = mapper.treeToValue(topLevel, Equity.class);
                equities.add(equity);
            }
            return Optional.of(equities);

        } catch (Exception e) {
            LOG.error("Could not retrieve quote for " + Arrays.toString(tickers), e);
        }
        return Optional.empty();
    }
}

package com.sleet.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleet.api.model.Equity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * A {@link Service} implementation that provides methods to retrieve equity quote data from the TD API
 *
 * TODO: Implement multiple quotes query
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
    public Equity getQuote(final String ticker) {

        final String url = API_URL + ticker + QUOTE_URL;
        try {
            final ResponseEntity<JsonNode> json = restTemplate.getForEntity(url, JsonNode.class);
            return mapper.readValue(json.getBody().get(ticker).toString(), Equity.class);
        } catch(Exception e) {
            LOG.error("Could not retrieve quote info for " + ticker, e);
        }
        return null;
    }
}

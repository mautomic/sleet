package com.sleet.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleet.api.HttpClient;
import com.sleet.api.model.Option;
import com.sleet.api.model.OptionChain;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Service} implementation that provides methods to retrieve option data from the TD API
 *
 * @author mautomic
 */
public class OptionService extends Service {

    private static String OPTION_CHAIN_URL;
    private static final String OTM = "&range=OTM";
    private static final String CONTRACT_TYPE = "&contractType=";
    private static final String SYMBOL = "&symbol=";
    private static final String STRIKE_COUNT = "&strikeCount=";
    private static final String STRIKE = "&strike=";
    private static final String FROM_DATE = "&fromDate=";
    private static final String TO_DATE = "&toDate=";
    private static final String DEFAULT_STRIKE_COUNT = "100"; // Count for above and below at-the-money, so x2 contracts are returned
    private static final Logger LOG = LoggerFactory.getLogger(OptionService.class);
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    public OptionService(final String apiKey) {
        httpClient = new HttpClient(DEFAULT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS);
        OPTION_CHAIN_URL = API_URL + "chains?apikey=" + apiKey;
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the default number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChain(final String ticker) {
        final CompletableFuture<OptionChain> future = getOptionChainAsync(ticker);
        try {
            return future.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint asynchronously for a ticker's option chain, getting the default number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @return {@link CompletableFuture} with an {@link OptionChain}
     */
    public CompletableFuture<OptionChain> getOptionChainAsync(final String ticker) {
        return getOptionChainAsync(ticker, DEFAULT_STRIKE_COUNT);
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the specified number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @param strikeCount of options to get in a single expiration period
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChain(final String ticker, final String strikeCount) {
        final CompletableFuture<OptionChain> future = getOptionChainAsync(ticker, strikeCount);
        try {
            return future.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint asynchronously for a ticker's option chain, getting the specified number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @param strikeCount of options to get in a single expiration period
     * @return {@link CompletableFuture} with an {@link OptionChain}
     */
    public CompletableFuture<OptionChain> getOptionChainAsync(final String ticker, final String strikeCount) {

        final StringBuilder builder = new StringBuilder()
                .append(OPTION_CHAIN_URL)
                .append(SYMBOL)
                .append(ticker)
                .append(STRIKE_COUNT)
                .append(strikeCount);

        final CompletableFuture<OptionChain> future = new CompletableFuture<>();
        httpClient.get(builder.toString()).whenComplete((response, exception) -> {
            future.complete(deserializeResponse(response));
        });
        return future;
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, filtering for contracts expiring before a specified date
     *
     * @param ticker of security to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOTMCloseExpirationOptionChain(final String ticker, final String furthestExpirationDate) {
        final CompletableFuture<OptionChain> future = getOTMCloseExpirationOptionChainAsync(ticker, furthestExpirationDate);
        try {
            return future.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint asynchronously for a ticker's option chain, filtering for contracts expiring before a specified date
     *
     * @param ticker of security to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today
     * @return {@link CompletableFuture} with an {@link OptionChain}
     */
    public CompletableFuture<OptionChain> getOTMCloseExpirationOptionChainAsync(final String ticker, final String furthestExpirationDate) {

        final StringBuilder builder = new StringBuilder()
                .append(OPTION_CHAIN_URL)
                .append(SYMBOL)
                .append(ticker)
                .append(TO_DATE)
                .append(furthestExpirationDate)
                .append(OTM);

        final CompletableFuture<OptionChain> future = new CompletableFuture<>();
        httpClient.get(builder.toString()).whenComplete((response, exception) -> {
            future.complete(deserializeResponse(response));
        });
        return future;
    }

    /**
     * Queries the TD API endpoint for all options for a ticker on a specific expiration date
     *
     * @param ticker of security to retrieve options for
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChainForDate(final String ticker, final String expirationDate) {
        final CompletableFuture<OptionChain> future = getOptionChainForDateAsync(ticker, expirationDate);
        try {
            return future.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint asynchronously for all options for a ticker on a specific expiration date
     *
     * @param ticker of security to retrieve options for
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return {@link CompletableFuture} with an {@link OptionChain}
     */
    public CompletableFuture<OptionChain> getOptionChainForDateAsync(final String ticker, final String expirationDate) {

        final StringBuilder builder = new StringBuilder()
                .append(OPTION_CHAIN_URL)
                .append(SYMBOL)
                .append(ticker)
                .append(STRIKE_COUNT)
                .append(DEFAULT_STRIKE_COUNT)
                .append(TO_DATE)
                .append(expirationDate)
                .append(FROM_DATE)
                .append(expirationDate);

        final CompletableFuture<OptionChain> future = new CompletableFuture<>();
        httpClient.get(builder.toString()).whenComplete((response, exception) -> {
            future.complete(deserializeResponse(response));
        });
        return future;
    }

    /**
     * Queries the TD API endpoint for all options for a ticker with a specific strike price
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChainForStrike(final String ticker, final int strike) {

        final CompletableFuture<OptionChain> future = getOptionChainForStrikeAsync(ticker, strike);
        try {
            return future.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint asynchronously for all options for a ticker with a specific strike price
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @return {@link CompletableFuture} with an {@link OptionChain}
     */
    public CompletableFuture<OptionChain> getOptionChainForStrikeAsync(final String ticker, final int strike) {

        final StringBuilder builder = new StringBuilder()
                .append(OPTION_CHAIN_URL)
                .append(SYMBOL)
                .append(ticker)
                .append(STRIKE)
                .append(strike);

        final CompletableFuture<OptionChain> future = new CompletableFuture<>();
        httpClient.get(builder.toString()).whenComplete((response, exception) -> {
            future.complete(deserializeResponse(response));
        });
        return future;
    }

    /**
     * Queries the TD API endpoint for options for a ticker on a specific expiration date with a specific strike
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChainForStrikeAndDate(final String ticker, final int strike, final String expirationDate) {

        final CompletableFuture<OptionChain> future = getOptionChainForStrikeAndDateAsync(ticker, strike, expirationDate);
        try {
            return future.get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint asynchronously for options for a ticker on a specific expiration date with a specific strike
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return {@link CompletableFuture} with an {@link OptionChain}
     */
    public CompletableFuture<OptionChain> getOptionChainForStrikeAndDateAsync(final String ticker, final int strike, final String expirationDate) {

        final StringBuilder builder = new StringBuilder()
                .append(OPTION_CHAIN_URL)
                .append(SYMBOL)
                .append(ticker)
                .append(TO_DATE)
                .append(expirationDate)
                .append(FROM_DATE)
                .append(expirationDate)
                .append(STRIKE)
                .append(strike);

        final CompletableFuture<OptionChain> future = new CompletableFuture<>();
        httpClient.get(builder.toString()).whenComplete((response, exception) -> {
            future.complete(deserializeResponse(response));
        });
        return future;
    }

    /**
     * Method to run two {@link OptionChain} requests concurrently, one just for calls, and one
     * just for puts. As the bottleneck for presenting large quantities of {@link Option}s back
     * to the caller is the GET request to TD, this effectively cuts the retrieval speed in half.
     * <p>
     * {@link OptionChain}s are returned in an async fashion via a {@link CompletableFuture}.
     *
     * @param urls to send GET requests
     * @return {@link OptionChain} for the original request
     */
    private OptionChain getCallsAndPutsConcurrently(final List<String> urls) {

        final List<CompletableFuture<OptionChain>> futures = Arrays.asList(new CompletableFuture<>(), new CompletableFuture<>());
        try {
            int index = 0;
            for (final String url : urls)
                fetchOptionChain(url, futures.get(index++));

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

            // Combine the two chains
            final OptionChain fullChain = futures.get(0).get();
            if (fullChain.getCallExpDateMap().isEmpty()) {
                fullChain.setCallExpDateMap(futures.get(1).get().getCallExpDateMap());
            } else {
                fullChain.setPutExpDateMap(futures.get(1).get().getPutExpDateMap());
            }
            return fullChain;
        } catch(final Exception e) {
            logFailure(e);
            return new OptionChain();
        }
    }

    /**
     * Asynchronously fetch a single {@link OptionChain} (or part of an OptionChain) for a particular URL
     *
     * @param url to send GET request
     * @param future to complete the OptionChain with
     */
    private void fetchOptionChain(final String url, final CompletableFuture<OptionChain> future) {
        httpClient.get(url, response -> {
            try {
                future.complete(deserializeResponse(response));
            } catch(Exception e) {
                future.complete(null);
            }
        });
    }

    /**
     * Deserialize a JSON response string into an {@link OptionChain}
     *
     * @param response to deserialize
     * @return {@link OptionChain} for the original request, or null if exception occurs
     */
    private OptionChain deserializeResponse(final Response response) {
        if (response.getStatusCode() == 200) {
            final String json = response.getResponseBody();
            try {
                return mapper.readValue(json, OptionChain.class);
            } catch(Exception e) {
                logFailure(e);
            }
        }
        return null;
    }

    /**
     * Method for logging exceptions after failed HTTP requests. Exit program if api key expires.
     */
    private void logFailure(final Exception e) {
        LOG.error("Could not retrieve option chain", e);
        if(e.getMessage().contains("InvalidApiKey"))
            System.exit(1);
    }
}

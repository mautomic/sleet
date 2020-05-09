package com.sleet.api.service;

import com.sleet.api.model.Contract;
import com.sleet.api.model.Option;
import com.sleet.api.model.OptionChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * A {@link Service} implementation that provides methods to retrieve option data from the TD API
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
    private static final StringBuilder builder = new StringBuilder();

    public OptionService(final String apiKey) {
        restTemplate = new RestTemplate(getClientHttpRequestFactory());
        OPTION_CHAIN_URL = API_URL + "chains?apikey=" + apiKey;
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, filtering for contracts expiring before a specified date
     *
     * @param ticker of security to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOTMCloseExpirationOptionChain(final String ticker, final String furthestExpirationDate) {

        try {
            final List<String> urls = new ArrayList<>();
            for (final Contract contract : Contract.values()) {

                resetBuilderUrl(ticker);
                builder.append(TO_DATE);
                builder.append(furthestExpirationDate);
                builder.append(OTM);
                builder.append(CONTRACT_TYPE);
                builder.append(contract.name());
                urls.add(builder.toString());
            }
            return getCallsAndPutsConcurrently(urls);

        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the default number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChain(final String ticker) {
        return getOptionChain(ticker, DEFAULT_STRIKE_COUNT);
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the specified number of strikes for each expiration
     *
     * @param ticker of security to retrieve options for
     * @param strikeCount of options to get in a single expiration period
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChain(final String ticker, final String strikeCount) {

        try {
            final List<String> urls = new ArrayList<>();
            for (final Contract contract : Contract.values()) {

                resetBuilderUrl(ticker);
                builder.append(STRIKE_COUNT);
                builder.append(strikeCount);
                builder.append(CONTRACT_TYPE);
                builder.append(contract.name());
                urls.add(builder.toString());
            }
            return getCallsAndPutsConcurrently(urls);

        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint for all options for a ticker on a specific expiration date
     *
     * @param ticker of security to retrieve options for
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChainForDate(final String ticker, final String expirationDate) {

        try {
            resetBuilderUrl(ticker);
            setBuilderDefaultStrikeCount();
            setBuilderExpirationDate(expirationDate);
            final String url = builder.toString();
            return restTemplate.getForObject(url, OptionChain.class);

        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint for all options for a ticker with a specific strike price
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChainForStrike(final String ticker, final int strike) {

        try {
            resetBuilderUrl(ticker);
            builder.append(STRIKE);
            builder.append(strike);
            final String url = builder.toString();

            return restTemplate.getForObject(url, OptionChain.class);

        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint for options for a ticker on a specific expiration date with a specific strike
     *
     * @param ticker of security to retrieve options for
     * @param strike of the options to retrieve
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChainForStrikeAndDate(final String ticker,
                                                      final int strike,
                                                      final String expirationDate) {
        try {
            resetBuilderUrl(ticker);
            setBuilderExpirationDate(expirationDate);
            builder.append(STRIKE);
            builder.append(strike);
            final String url = builder.toString();
            return restTemplate.getForObject(url, OptionChain.class);

        } catch(Exception e) {
            logFailure(e);
        }
        return null;
    }

    /**
     * Method to run two requests concurrently on their own {@link Thread}, one just for calls, and one just for puts.
     * As the bottleneck for presenting large quantities of {@link Option}s back to the caller is the GET request to TD,
     * this effectively cuts the retrieval speed in half.
     *
     * @param urls to send GET requests
     * @return {@link OptionChain} for the original request
     */
    private OptionChain getCallsAndPutsConcurrently(final List<String> urls) {

        final CountDownLatch latch = new CountDownLatch(2);
        final List<OptionChain> chainList = new ArrayList<>(2);

        try {
            for (final String url : urls) {
                new Thread(() -> {
                    final OptionChain contractChain = restTemplate.getForObject(url, OptionChain.class);
                    chainList.add(contractChain);
                    latch.countDown();
                }).start();
            }
            latch.await();

        } catch(final Exception e) {
            logFailure(e);
        }

        // Combine the two chains
        final OptionChain fullChain = chainList.get(0);
        if (fullChain.getCallExpDateMap().isEmpty()) {
            fullChain.setCallExpDateMap(chainList.get(1).getCallExpDateMap());
        } else {
            fullChain.setPutExpDateMap(chainList.get(1).getPutExpDateMap());
        }
        return fullChain;
    }

    /**
     * Method for logging exceptions after failed HTTP requests. Exit program if api key expires.
     */
    private void logFailure(final Exception e) {
        LOG.error("Could not retrieve option chain", e);
        if(e.getMessage().contains("InvalidApiKey"))
            System.exit(1);
    }

    /**
     * Reset StringBuilder for url generation and add ticker, which is always required for a request
     *
     * @param ticker of security to retrieve options for
     */
    private void resetBuilderUrl(final String ticker) {
        builder.setLength(0);
        builder.append(OPTION_CHAIN_URL);
        builder.append(SYMBOL);
        builder.append(ticker);
    }

    /**
     * Add toDate and fromDate to builder
     *
     * @param expirationDate of the options to retrieve, must follow the format of yyyy-MM-dd
     */
    private void setBuilderExpirationDate(final String expirationDate) {
        builder.append(TO_DATE);
        builder.append(expirationDate);
        builder.append(FROM_DATE);
        builder.append(expirationDate);
    }

    /**
     * Add default strike count to builder
     */
    private void setBuilderDefaultStrikeCount() {
        builder.append(STRIKE_COUNT);
        builder.append(DEFAULT_STRIKE_COUNT);
    }
}

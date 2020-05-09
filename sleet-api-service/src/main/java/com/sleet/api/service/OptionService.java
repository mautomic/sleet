package com.sleet.api.service;

import com.sleet.api.domain.OptionChain;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * A {@link Service} implementation that provides methods to retrieve option data from the TD API
 */
public class OptionService extends Service {

    private static String OPTION_CHAIN_URL;
    private static final String DEFAULT_STRIKE_COUNT = "50";
    private static final Logger LOG = LogManager.getLogger(OptionService.class);

    public OptionService(String API_KEY) {
        restTemplate = new RestTemplate();
        OPTION_CHAIN_URL = API_URL + "/chains?apikey=" + API_KEY;
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, filtering for contracts expiring before a specified date
     *
     * @param ticker of asset to retrieve options for
     * @param furthestExpirationDate of options to retrieve starting from today
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getCloseExpirationOptionChain(String ticker, String furthestExpirationDate) {

        try {
            LOG.info("Requesting Option Chain ");
            final OptionChain optionChain = restTemplate.getForObject(OPTION_CHAIN_URL + "&symbol=" + ticker + "&strikeCount=" +
                    DEFAULT_STRIKE_COUNT + "&toDate=" + furthestExpirationDate, OptionChain.class);
            LOG.info("Fetched Option Chain");
            return optionChain;

        } catch(Exception e) {
            LOG.error("Could not retrieve option chain", e);
            if(e.getMessage().contains("InvalidApiKey"))
                System.exit(1);
        }
        return null;
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the default number of strikes for each expiration
     *
     * @param ticker of asset to retrieve options for
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChain(String ticker) {
        return getOptionChain(ticker, DEFAULT_STRIKE_COUNT);
    }

    /**
     * Queries the TD API endpoint for a ticker's option chain, getting the specified number of strikes for each expiration
     *
     * @param ticker of asset to retrieve options for
     * @param strikeCount of options to get in a single expiration period
     * @return {@link OptionChain} with all option data for the ticker
     */
    public OptionChain getOptionChain(String ticker, String strikeCount) {

        try {
            LOG.info("Requesting Option Chain ");
            final OptionChain optionChain = restTemplate.getForObject(OPTION_CHAIN_URL + "&symbol=" + ticker +
                    "&strikeCount=" + strikeCount, OptionChain.class);
            LOG.info("Fetched Option Chain");
            return optionChain;

        } catch(Exception e) {
            LOG.error("Could not retrieve option chain", e);
            if(e.getMessage().contains("InvalidApiKey"))
                System.exit(1);
        }
        return null;
    }
}

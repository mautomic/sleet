package com.sleet.api.service;

import com.sleet.api.domain.OptionChain;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class OptionService extends Service {

    private String OPTION_CHAIN_URL;
    private static final Logger LOG = LogManager.getLogger(OptionService.class);

    public OptionService(String API_KEY) {

        restTemplate = new RestTemplate();
        OPTION_CHAIN_URL = API_URL + "/chains?apikey=" + API_KEY;
    }

    public OptionChain getOptionChain(String ticker) {

        return getOptionChain(ticker, "20");
    }

    public OptionChain getOptionChain(String ticker, String strikeCount) {

        try {
            LOG.info("Requesting Option Chain ");
            OptionChain optionChain = restTemplate.getForObject(OPTION_CHAIN_URL + "&symbol=" + ticker + "&strikeCount=" + strikeCount, OptionChain.class);
            LOG.info("Fetched Option Chain");
            return optionChain;

        } catch(Exception e) {
            LOG.error("Could not retrieve option chain: " + e.getMessage());
        }

        return null;
    }

}

package service;

import org.springframework.http.ResponseEntity;
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

    public String getOptionChain(String ticker) {

        return getOptionChain(ticker, "3");
    }

    public String getOptionChain(String ticker, String strikeCount) {

        ResponseEntity<String> response;
        String optionChain = null;

        try {
            response = restTemplate.getForEntity(OPTION_CHAIN_URL + "&symbol=" + ticker + "&strikeCount=" + strikeCount, String.class);
            optionChain = response.getBody();
        } catch(Exception e) {
            LOG.error("Could not retrieve option chain: " + e.getMessage());
        }

        return optionChain;
    }

}

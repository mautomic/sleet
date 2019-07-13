package service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OptionService extends Service {

    final String OPTION_CHAIN_URL = API_URL + "/chains?apikey=" + API_KEY;

    public OptionService() {

        restTemplate = new RestTemplate();
    }

    public String getOptionChain(String ticker) {

        return getOptionChain(ticker, "10");
    }

    public String getOptionChain(String ticker, String strikeCount) {

        ResponseEntity<String> response;
        String optionChain = null;

        try {
            response = restTemplate.getForEntity(OPTION_CHAIN_URL + "&symbol=" + ticker + "&strikeCount=" + strikeCount, String.class);
            optionChain = response.getBody();
        } catch(Exception e) {}

        return optionChain;
    }

}

import domain.Option;
import domain.OptionChain;
import service.OptionService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main (String[] args) {

        LOG.info("Initializing Sleet.......");

        LOG.info("Retrieving API key");
        Authenticator authenticator = Authenticator.getInstance();

        OptionService optionService = new OptionService(authenticator.getApiKey());
        OptionChain optionChain = optionService.getOptionChain("SPY");
        Option option = optionChain.getCallExpDateMap().get("2019-07-22:8").get("300.0").get(0);

        LOG.info("Option Description: " + option.getDescription());
        LOG.info("Price: " + option.getMark());
    }
}

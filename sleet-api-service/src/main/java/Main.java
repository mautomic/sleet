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
        String optionChain = optionService.getOptionChain("SPY");

        LOG.info(optionChain);
    }
}

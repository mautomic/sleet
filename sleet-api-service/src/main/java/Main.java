import domain.Option;
import domain.OptionChain;
import service.OptionService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main (String[] args) {

        LOG.info("Initializing Sleet.......");

        LOG.info("Retrieving API key");
        Authenticator authenticator = Authenticator.getInstance();
        OptionService optionService = new OptionService(authenticator.getApiKey());

        Runnable logOptionPricesByMinute = () -> {

            OptionChain optionChain = optionService.getOptionChain("SPY");
            Map<String, Map<String, List<Option>>> callMap = optionChain.getCallExpDateMap();

            Set<String> dates = callMap.keySet();
            Set<String> strikes;

            for (String date : dates) {

                LOG.info("Expiration Date : " + date);
                strikes = callMap.get(date).keySet();

                for (String strike : strikes)
                    LOG.info(callMap.get(date).get(strike).get(0).toString());
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(logOptionPricesByMinute, 5, 60, TimeUnit.SECONDS);
    }
}

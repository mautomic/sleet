import domain.Option;
import domain.OptionChain;
import service.OptionService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main (String[] args) throws Exception {

        LOG.info("Initializing Sleet.......");

        LOG.info("Retrieving API key");
        Authenticator authenticator = Authenticator.getInstance();
        OptionService optionService = new OptionService(authenticator.getApiKey());

        String dailyTimestamp = new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());

        Scanner sc = new Scanner(new File(Main.class.getResource("resources.cfg").getFile()));
        String logLocation = sc.next();

        File logFile = new File(logLocation + dailyTimestamp + ".txt");
        logFile.createNewFile();
        LOG.info("Created daily log file : " + logFile.getName());

        BufferedWriter pricingLogger = new BufferedWriter(new FileWriter(logFile), 32768);

        Runnable logOptionPricesByMinute = () -> {

            OptionChain optionChain = optionService.getOptionChain("SPY");
            Map<String, Map<String, List<Option>>> callMap = optionChain.getCallExpDateMap();

            String periodicTimestamp = new SimpleDateFormat("HH.mm.ss").format(new Date());

            Set<String> dates = callMap.keySet();
            Set<String> strikes;

            for (String date : dates) {

                try {
                    pricingLogger.write(periodicTimestamp + " : Expiration Date : " + date);
                    pricingLogger.newLine();
                } catch (IOException e) {
                    LOG.info("Cannot write prices to file");
                }

                strikes = callMap.get(date).keySet();

                for (String strike : strikes) {

                    try {
                        pricingLogger.write(periodicTimestamp + " : " + callMap.get(date).get(strike).get(0).toString());
                        pricingLogger.newLine();
                    } catch (IOException e) {
                        LOG.info("Cannot write prices to file");
                    }
                }
            }

            LOG.info("Logged option prices at: " + periodicTimestamp);
        };

        LOG.info("Starting scheduled executor");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(logOptionPricesByMinute, 5, 60, TimeUnit.SECONDS);
    }
}

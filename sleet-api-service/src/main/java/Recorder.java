import domain.Option;
import domain.OptionChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.OptionService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Recorder {

    private static final Logger LOG = LogManager.getLogger(Recorder.class);
    private static final int API_REQUEST_INTERVAL = 10;
    private static final int DELAY = 5;

    public Recorder() {}

    public void recordData() throws Exception {

        LOG.info("Retrieving API key");
        Authenticator authenticator = Authenticator.getInstance();
        OptionService optionService = new OptionService(authenticator.getApiKey());

        String dailyTimestamp = new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());

        // TODO: Proper resource retrieval
        Scanner sc = new Scanner(new File(Main.class.getResource("resources.cfg").getFile()));
        String optionLogLocation = sc.next();
        String spreadLogLocation = sc.next();

        File optionPriceFile = new File(optionLogLocation + dailyTimestamp + ".txt");
        optionPriceFile.createNewFile();
        LOG.info("Created daily log file : " + optionPriceFile.getName());

        File spreadPriceFile = new File(spreadLogLocation + dailyTimestamp + ".txt");
        spreadPriceFile.createNewFile();
        LOG.info("Created daily log file : " + spreadPriceFile.getName());

        BufferedWriter optionPricingLogger = new BufferedWriter(new FileWriter(optionPriceFile), 32768);
        BufferedWriter spreadPricingLogger = new BufferedWriter(new FileWriter(spreadPriceFile), 32768);

        Runnable logDataByMinute = () -> {

            OptionChain optionChain = optionService.getOptionChain("SPY");
            Map<String, Map<String, List<Option>>> callMap = optionChain.getCallExpDateMap();
            Map<String, Map<String, List<Option>>> putMap = optionChain.getPutExpDateMap();

            try {

                // TODO: Record Calls and Puts from same expiry together
                recordOptionPrices(callMap, "CALL", optionPricingLogger);
                recordOptionPrices(putMap, "PUT", optionPricingLogger);

                recordSpreadPrices(callMap, "CALL", spreadPricingLogger);
                recordSpreadPrices(putMap, "PUT", spreadPricingLogger);
            }
            catch (Exception e) {
                LOG.warn("Issue Writing to Logs");
            }

        };

        LOG.info("Starting scheduled executor");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(logDataByMinute, DELAY, API_REQUEST_INTERVAL, TimeUnit.SECONDS);
    }

    private void recordOptionPrices(Map<String, Map<String, List<Option>>> optionMap, String type, BufferedWriter bfwriter) {

        String periodicTimestamp = new SimpleDateFormat("HH.mm.ss").format(new Date());

        Set<String> dates = optionMap.keySet();
        Set<String> strikes;

        for (String date : dates) {

            try {

                bfwriter.write(periodicTimestamp + " : Expiration Date : " + date + " : " + type);
                bfwriter.newLine();

                strikes = optionMap.get(date).keySet();

                for (String strike : strikes)
                    bfwriter.write(optionMap.get(date).get(strike).get(0).toString() + ", ");

                bfwriter.newLine();

            } catch (Exception e) {
                LOG.error("Issue writing prices to logs");
            }
        }

        LOG.info("Logged " + type + " option prices at: " + periodicTimestamp);
    }

    private void recordSpreadPrices(Map<String, Map<String, List<Option>>> optionMap, String type, BufferedWriter bfwriter) {

        String periodicTimestamp = new SimpleDateFormat("HH.mm.ss").format(new Date());

        Set<String> dates = optionMap.keySet();
        List<String> strikes;

        for (String date : dates) {

            try {

                bfwriter.write(periodicTimestamp + " : Expiration Date : " + date + " : " + type);
                bfwriter.newLine();

                strikes = new ArrayList<>(optionMap.get(date).keySet());

                for (int i = 0; i < strikes.size() - 1; i++) {
                    for (int j = i + 1; j < strikes.size(); j++) {

                        Option shortLeg;
                        Option longLeg;

                        if (type.equalsIgnoreCase("CALL")) {
                            shortLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                            longLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                        } else {
                            shortLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                            longLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                        }

                        double spreadPrice;

                        spreadPrice = shortLeg.getMark() - longLeg.getMark();

                        BigDecimal roundedSpreadPrice = new BigDecimal(spreadPrice);
                        roundedSpreadPrice = roundedSpreadPrice.setScale(2, RoundingMode.HALF_UP);

                        bfwriter.write(shortLeg.getStrikePrice() + "/" + longLeg.getStrikePrice() + " : " + roundedSpreadPrice + ", ");

                    }
                }

                bfwriter.newLine();

            } catch (NullPointerException | IOException e) {
                LOG.info("Error logging spread pricing");
            }
        }

        LOG.info("Logged " + type + " spread prices at: " + periodicTimestamp);
    }
}

import domain.Option;
import domain.OptionChain;
import service.OptionService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    private static final int API_REQUEST_INTERVAL = 60;
    private static final int DELAY = 5;

    public static void main (String[] args) throws Exception {

        LOG.info("Initializing Sleet.......");

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
            catch (IOException e) {}

        };

        LOG.info("Starting scheduled executor");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(logDataByMinute, DELAY, API_REQUEST_INTERVAL, TimeUnit.SECONDS);
    }

    public static void recordOptionPrices(Map<String, Map<String, List<Option>>> optionMap, String type, BufferedWriter bfwriter) throws IOException {

        String periodicTimestamp = new SimpleDateFormat("HH.mm.ss").format(new Date());

        Set<String> dates = optionMap.keySet();
        Set<String> strikes;

        for (String date : dates) {

            try {
                bfwriter.write(periodicTimestamp + " : Expiration Date : " + date + " : " + type);
                bfwriter.newLine();
            } catch (IOException e) {
                LOG.info("Cannot write prices to file");
            }

            strikes = optionMap.get(date).keySet();

            for (String strike : strikes) {

                try {
                    bfwriter.write(periodicTimestamp + " : " + optionMap.get(date).get(strike).get(0).toString());
                    bfwriter.newLine();
                } catch (IOException e) {
                    LOG.info("Cannot write prices to file");
                }
            }
        }

        LOG.info("Logged " + type + " option prices at: " + periodicTimestamp);
    }

    public static void recordSpreadPrices(Map<String, Map<String, List<Option>>> optionMap, String type, BufferedWriter bfwriter) throws IOException {

        String periodicTimestamp = new SimpleDateFormat("HH.mm.ss").format(new Date());

        Set<String> dates = optionMap.keySet();
        List<String> strikes;

        for (String date : dates) {

            try {
                bfwriter.write(periodicTimestamp + " : Expiration Date : " + date + " : " + type);
                bfwriter.newLine();
            } catch (IOException e) {
                LOG.info("Cannot write prices to file");
            }

            strikes = new ArrayList<>(optionMap.get(date).keySet());

            for(int i=0; i<strikes.size()-1; i++) {
                for(int j=i+1; j<strikes.size(); j++) {

                    Option shortLeg;
                    Option longLeg;

                    if (type.equalsIgnoreCase("CALL")) {
                        shortLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                        longLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                    } else {
                        shortLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                        longLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                    }

                    double spreadPrice = shortLeg.getMark() - longLeg.getMark();
                    BigDecimal roundedSpreadPrice = new BigDecimal(spreadPrice);
                    roundedSpreadPrice = roundedSpreadPrice.setScale(2, RoundingMode.HALF_UP);

                    bfwriter.write(shortLeg.getStrikePrice() + "/" + longLeg.getStrikePrice() + " : " + roundedSpreadPrice);
                    bfwriter.newLine();
                }
            }
        }

        LOG.info("Logged " + type + " spread prices at: " + periodicTimestamp);
    }
}

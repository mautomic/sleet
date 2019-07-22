package com.sleet.tools;

import com.sleet.api.domain.Option;
import com.sleet.api.domain.OptionChain;
import com.sleet.api.service.OptionService;
import com.sleet.tools.objects.Spread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpreadScanner {

    private static final Logger LOG = LogManager.getLogger(SpreadScanner.class);
    private static final int API_REQUEST_INTERVAL = 60;
    private static final int DELAY = 5;

    public static void main(String[]args) {

        LOG.info("Initializing spread screener.......");

        LOG.info("Retrieving API key");
        OptionService optionService = new OptionService(GlobalProperties.getInstance().getApiKey());

        Runnable logDataByMinute = () -> {

            OptionChain optionChain = optionService.getOptionChain("SPX");
            Map<String, Map<String, List<Option>>> callMap = optionChain.getCallExpDateMap();
            Map<String, Map<String, List<Option>>> putMap = optionChain.getPutExpDateMap();

            List<Spread> callSpreadList = getSpreadList(callMap, "CALL");

            for(Spread spread : callSpreadList)
                LOG.info(spread.toString());
        };

        LOG.info("Starting scheduled executor");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(logDataByMinute, DELAY, API_REQUEST_INTERVAL, TimeUnit.SECONDS);
    }

    private static List<Spread> getSpreadList(Map<String, Map<String, List<Option>>> optionMap, String type) {

        Set<String> dates = optionMap.keySet();
        List<String> strikes;

        List<Spread> spreads = new ArrayList<>();

        for (String date : dates) {

            strikes = new ArrayList<>(optionMap.get(date).keySet());

            for (int i = 0; i < strikes.size() - 1; i++) {
                for (int j = i + 1; j < strikes.size(); j++) {

                    Option shortLeg;
                    Option longLeg;

                    try {

                        if (type.equalsIgnoreCase("CALL")) {
                            shortLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                            longLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                        } else {
                            shortLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                            longLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                        }

                        String spreadName = shortLeg.getStrikePrice() + "/" + longLeg.getStrikePrice();
                        int expirationDate = Integer.parseInt(date.substring(date.indexOf(":")+1));

                        double spreadPrice = shortLeg.getMark() - longLeg.getMark();
                        BigDecimal roundedSpreadPrice = new BigDecimal(spreadPrice).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal multiplier = new BigDecimal(100);

                        BigDecimal roundedBuyingPower = new BigDecimal(shortLeg.getStrikePrice() - longLeg.getStrikePrice());
                        roundedBuyingPower = roundedBuyingPower.multiply(multiplier).abs().setScale(2, RoundingMode.HALF_UP);

                        BigDecimal roundedRoi = roundedSpreadPrice.multiply(multiplier).divide(roundedBuyingPower, 3, RoundingMode.HALF_UP);

                        Spread spread = new Spread(spreadName, expirationDate, roundedSpreadPrice.doubleValue(),
                                roundedBuyingPower.intValue(), roundedRoi.doubleValue());

                        spreads.add(spread);

                    } catch(Exception e) {
                        LOG.error(e.getMessage());
                    }
                }
            }
        }

        return spreads;
    }
}
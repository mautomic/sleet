package com.sleet.tools;

import com.sleet.api.domain.Option;
import com.sleet.api.domain.OptionChain;
import com.sleet.api.service.OptionService;
import com.sleet.tools.objects.Contract;
import com.sleet.tools.objects.Spread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpreadRetrieverTask implements Runnable {

    private final String ticker;
    private final OptionService optionService;
    private static final int API_REQUEST_INTERVAL = 20;
    private static final int DELAY = 2;
    private static final int DAYS_TO_EXPIRATION = 40;
    private static String expirationDate;
    private static final Logger LOG = LogManager.getLogger(SpreadRetrieverTask.class);

    public SpreadRetrieverTask(OptionService optionService, String ticker) {
        this.optionService = optionService;
        this.ticker = ticker;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, DAYS_TO_EXPIRATION);
        expirationDate = sdf.format(cal.getTime());
    }

    @Override
    public void run() {

        Runnable retrieveSpreads = () -> {

            OptionChain optionChain = optionService.getCloseExpirationOptionChain(ticker, expirationDate);
            Map<String, Map<String, List<Option>>> callMap = optionChain.getCallExpDateMap();
            Map<String, Map<String, List<Option>>> putMap = optionChain.getPutExpDateMap();

            List<Spread> callSpreadList = getSpreadList(callMap, Contract.CALL.name());
            List<Spread> putSpreadList = getSpreadList(putMap, Contract.PUT.name());

            Collections.sort(putSpreadList);

            for(Spread spread : putSpreadList)
                if (spread.getBuyingPower() <= 5000 && spread.getRoi() >= 0.25)
                    LOG.info(spread.toString());

        };

        LOG.info("Starting scheduled executor for " + ticker);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(retrieveSpreads, DELAY, API_REQUEST_INTERVAL, TimeUnit.SECONDS);
    }

    private List<Spread> getSpreadList(Map<String, Map<String, List<Option>>> optionMap, String type) {

        Set<String> dates = optionMap.keySet();
        List<String> strikes;

        List<Spread> spreads = new ArrayList<>();

        for (String date : dates) {

            if(date.split(":")[1].equalsIgnoreCase("0"))
                continue;

            strikes = new ArrayList<>(optionMap.get(date).keySet());

            // Only OTM options
            if (type.equalsIgnoreCase(Contract.CALL.name()))
                strikes = strikes.subList(strikes.size()/2, strikes.size());
            else
                strikes = strikes.subList(0, strikes.size()/2);

            for (int i = 0; i < strikes.size() - 1; i++) {
                for (int j = i + 1; j < strikes.size(); j++) {

                    Option shortLeg;
                    Option longLeg;

                    try {

                        if (type.equalsIgnoreCase(Contract.CALL.name())) {
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

                        Spread spread = new Spread(ticker, spreadName, expirationDate, roundedSpreadPrice.doubleValue(),
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

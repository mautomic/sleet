package com.sleet.tools;

import com.sleet.api.domain.Option;
import com.sleet.api.domain.OptionChain;
import com.sleet.api.service.OptionService;
import com.sleet.tools.objects.Contract;
import com.sleet.tools.objects.Spread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled runnable to retrieve an option chain and calculate spreads at the defined interval. A SpreadTaskRetriever
 * is meant to be created on its own non-blocking thread.
 */
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

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, DAYS_TO_EXPIRATION);
        expirationDate = sdf.format(cal.getTime());
    }

    @Override
    public void run() {

        Runnable retrieveSpreads = () -> {

            final OptionChain optionChain = optionService.getCloseExpirationOptionChain(ticker, expirationDate);
            final Map<String, Map<String, List<Option>>> callMap = optionChain.getCallExpDateMap();
            final Map<String, Map<String, List<Option>>> putMap = optionChain.getPutExpDateMap();

            final List<Spread> callSpreadList = getSpreadList(callMap, Contract.CALL);
            final List<Spread> putSpreadList = getSpreadList(putMap, Contract.PUT);

            Collections.sort(putSpreadList);

            for(Spread spread : putSpreadList)
                if (spread.getBuyingPower() <= 5000 && spread.getRoi() >= 0.25)
                    LOG.info(spread.toString());

        };

        LOG.info("Starting scheduled executor for " + ticker);
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(retrieveSpreads, DELAY, API_REQUEST_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * Generate all possible spreads for a chain of single options
     *
     * @param optionMap of single options for different strikes and expirations
     * @param contract of type CALL or PUT
     * @return a list of {@link Spread} objects
     */
    private List<Spread> getSpreadList(Map<String, Map<String, List<Option>>> optionMap, Contract contract) {

        final Set<String> dates = optionMap.keySet();
        List<String> strikes;

        final List<Spread> spreads = new ArrayList<>();

        for (String date : dates) {

            if(date.split(":")[1].equalsIgnoreCase("0"))
                continue;

            strikes = new ArrayList<>(optionMap.get(date).keySet());

            // Only OTM options
            if (contract.name().equalsIgnoreCase(Contract.CALL.name()))
                strikes = strikes.subList(strikes.size()/2, strikes.size());
            else
                strikes = strikes.subList(0, strikes.size()/2);

            for (int i = 0; i < strikes.size() - 1; i++) {
                for (int j = i + 1; j < strikes.size(); j++) {

                    Option shortLeg;
                    Option longLeg;

                    try {

                        if (contract.name().equalsIgnoreCase(Contract.CALL.name())) {
                            shortLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                            longLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                        } else {
                            shortLeg = optionMap.get(date).get(strikes.get(j)).get(0);
                            longLeg = optionMap.get(date).get(strikes.get(i)).get(0);
                        }

                        spreads.add(new Spread(shortLeg, longLeg));

                    } catch(Exception e) {
                        LOG.error("Error creating spread",e);
                    }
                }
            }
        }
        return spreads;
    }
}

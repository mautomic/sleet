package com.sleet.tools;

import com.sleet.api.service.OptionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Retrieve and filter for optimal or mis-priced option spreads at high frequency intervals during the trading day
 */
public class SpreadScanner {

    private static final Logger LOG = LogManager.getLogger(SpreadScanner.class);
    private static final String[] WATCHLIST = {"SPY", "QQQ", "VIX", "TLT"};

    public static void main(String[]args) {

        LOG.info("Initializing spread screener.......");

        LOG.info("Retrieving API key");
        OptionService optionService = new OptionService(GlobalProperties.getInstance().getApiKey());

        for(String ticker : WATCHLIST) {

            final SpreadRetrieverTask task = new SpreadRetrieverTask(optionService, ticker);
            new Thread(task).start();
        }
    }
}
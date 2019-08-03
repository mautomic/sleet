package com.sleet.tools;

import com.sleet.api.service.OptionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpreadScanner {

    private static final Logger LOG = LogManager.getLogger(SpreadScanner.class);
    private static final String[] WATCHLIST = {"SPX", "SPY", "QQQ", "VIX", "TLT"};

    public static void main(String[]args) {

        LOG.info("Initializing spread screener.......");

        LOG.info("Retrieving API key");
        OptionService optionService = new OptionService(GlobalProperties.getInstance().getApiKey());

        for(String ticker : WATCHLIST) {

            SpreadRetrieverTask task = new SpreadRetrieverTask(optionService, ticker);
            new Thread(task).start();
        }
    }
}
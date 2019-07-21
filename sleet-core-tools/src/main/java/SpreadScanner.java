package com.sleet.tools;

import com.sleet.api.domain.Option;
import com.sleet.api.domain.OptionChain;
import com.sleet.api.service.OptionService;
import com.sleet.tools.recorders.OptionRecorder;
import com.sleet.tools.recorders.Recorder;
import com.sleet.tools.recorders.SpreadRecorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpreadScanner {

    private static final Logger LOG = LogManager.getLogger(SpreadScanner.class);
    private static final int API_REQUEST_INTERVAL = 10;
    private static final int DELAY = 5;

    public static void main(String[]args) {

        LOG.info("Initializing spread screener.......");

        LOG.info("Retrieving API key");
        OptionService optionService = new OptionService(GlobalProperties.getInstance().getApiKey());

        Recorder optionRecorder = new OptionRecorder();
        Recorder spreadRecorder = new SpreadRecorder();

        Runnable logDataByMinute = () -> {

            OptionChain optionChain = optionService.getOptionChain("SPY");
            Map<String, Map<String, List<Option>>> callMap = optionChain.getCallExpDateMap();
            Map<String, Map<String, List<Option>>> putMap = optionChain.getPutExpDateMap();

            try {

                // TODO: Record Calls and Puts from same expiry together
                optionRecorder.recordData(callMap, "CALL");
                optionRecorder.recordData(putMap, "PUT");

                spreadRecorder.recordData(callMap, "CALL");
                spreadRecorder.recordData(putMap, "PUT");
            }
            catch (Exception e) {
                LOG.warn("Issue Writing to Logs");
            }
        };

        LOG.info("Starting scheduled executor");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(logDataByMinute, DELAY, API_REQUEST_INTERVAL, TimeUnit.SECONDS);

    }
}
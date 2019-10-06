package com.sleet.tools.recorders;

import com.sleet.api.domain.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A recorder to log option prices
 */
public class OptionRecorder extends Recorder {

    private static final Logger LOG = LogManager.getLogger(OptionRecorder.class);
    private static String optionLogPath = logPath + "option-log-";
    File optionPriceFile;

    public OptionRecorder() {

        dailyTimestamp = new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());

        try {
            optionPriceFile = new File(optionLogPath + dailyTimestamp + ".txt");
            optionPriceFile.createNewFile();
            LOG.info("Created daily log file : " + optionPriceFile.getName());

            bufferedWriter = new BufferedWriter(new FileWriter(optionPriceFile), 32768);

        } catch(IOException e) {
            LOG.error("Cannot create log file");
        }

    }

    public void recordData(Map<String, Map<String, List<Option>>> optionMap, String type) {

        String periodicTimestamp = new SimpleDateFormat("HH.mm.ss").format(new Date());

        Set<String> dates = optionMap.keySet();
        Set<String> strikes;

        for (String date : dates) {

            try {

                bufferedWriter.write(periodicTimestamp + " : Expiration Date : " + date + " : " + type);
                bufferedWriter.newLine();

                strikes = optionMap.get(date).keySet();

                for (String strike : strikes)
                    bufferedWriter.write(optionMap.get(date).get(strike).get(0).toString() + ", ");

                bufferedWriter.newLine();

            } catch (Exception e) {
                LOG.error("Issue writing prices to logs");
            }
        }

        LOG.info("Logged " + type + " option prices at: " + periodicTimestamp);
    }
}
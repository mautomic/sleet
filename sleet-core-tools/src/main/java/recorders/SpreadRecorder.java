package com.sleet.tools.recorders;

import com.sleet.api.domain.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class SpreadRecorder extends Recorder {

    private static final Logger LOG = LogManager.getLogger(SpreadRecorder.class);
    private static String spreadLogPath = logPath + "spread-log-";
    File spreadPriceFile;

    public SpreadRecorder() {

        dailyTimestamp = new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());

        try {
            spreadPriceFile = new File(spreadLogPath + dailyTimestamp + ".txt");
            spreadPriceFile.createNewFile();
            LOG.info("Created daily log file : " + spreadPriceFile.getName());

            bufferedWriter = new BufferedWriter(new FileWriter(spreadPriceFile), 32768);

        } catch(IOException e) {
            LOG.error("Cannot create log file");
        }

    }

    public void recordData(Map<String, Map<String, List<Option>>> optionMap, String type) {

        String periodicTimestamp = new SimpleDateFormat("HH.mm.ss").format(new Date());

        Set<String> dates = optionMap.keySet();
        List<String> strikes;

        for (String date : dates) {

            try {

                bufferedWriter.write(periodicTimestamp + " : Expiration Date : " + date + " : " + type);
                bufferedWriter.newLine();

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

                        bufferedWriter.write(shortLeg.getStrikePrice() + "/" + longLeg.getStrikePrice() + " : " + roundedSpreadPrice + ", ");
                    }
                }

                bufferedWriter.newLine();

            } catch (NullPointerException | IOException e) {
                LOG.info("Error logging spread pricing");
            }
        }

        LOG.info("Logged " + type + " spread prices at: " + periodicTimestamp);
    }
}

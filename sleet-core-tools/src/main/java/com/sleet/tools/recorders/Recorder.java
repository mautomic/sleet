package com.sleet.tools.recorders;

import com.sleet.api.domain.Option;
import com.sleet.tools.GlobalProperties;

import java.io.BufferedWriter;
import java.util.List;
import java.util.Map;

/**
 * A recorder is used for logging real-time data to files
 */
public abstract class Recorder {

    static String logPath = GlobalProperties.getInstance().getlogPath();
    String dailyTimestamp;
    BufferedWriter bufferedWriter;

    public Recorder() {}

    public abstract void recordData(Map<String, Map<String, List<Option>>> optionMap, String type);
}
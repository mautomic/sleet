package com.sleet.api;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main (String[] args) throws Exception {

        LOG.info("Initializing Sleet.......");
        Recorder recorder = new Recorder();
        recorder.recordData();

    }
}

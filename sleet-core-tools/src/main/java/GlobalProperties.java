package com.sleet.tools;

import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GlobalProperties {

    private static GlobalProperties instance;
    private final Properties properties;
    private static final Logger LOG = LogManager.getLogger(GlobalProperties.class);

    public synchronized static GlobalProperties getInstance() {
        if (instance == null) {
            instance = new GlobalProperties();
        }
        return instance;
    }

    private GlobalProperties() {

        properties = new Properties();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources.cfg");
            properties.load(inputStream);

        } catch (Exception e) {
            LOG.error("Resources file not found");
        }
    }

    public String getApiKey() {
        return properties.getProperty("apiKey");
    }

    public String getOptionLogPath() {
        return properties.getProperty("optionLogPath");
    }

    public String getSpreadLogPath() {
        return properties.getProperty("spreadLogPath");
    }
}
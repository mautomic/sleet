package com.sleet.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleet.api.HttpClient;

/**
 * An abstract implementation of a service class can connect to different TD API endpoints
 *
 * @author mautomic
 */
public abstract class Service {

    HttpClient httpClient;
    static final int DEFAULT_TIMEOUT_MILLIS = 5000;
    final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    final static String API_URL = "https://api.tdameritrade.com/v1/";
    final static String MARKETDATA = "marketdata";
}

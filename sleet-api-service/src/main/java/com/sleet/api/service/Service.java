package com.sleet.api.service;

import com.sleet.api.HttpClient;

/**
 * An abstract implementation of a service class can connect to different TD API endpoints
 */
public abstract class Service {

    HttpClient httpClient;
    static final int DEFAULT_TIMEOUT_MILLIS = 5000;
    final static String API_URL = "https://api.tdameritrade.com/v1/marketdata/";
}

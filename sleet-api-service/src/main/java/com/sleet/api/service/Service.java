package com.sleet.api.service;

import org.springframework.web.client.RestTemplate;

/**
 * A service class can connect to different TD API endpoints. All reusable/shared objects are stored here.
 */
public abstract class Service {

    RestTemplate restTemplate;
    final static String API_URL = "https://api.tdameritrade.com/v1/marketdata";
}

package com.sleet.api.service;

import org.springframework.web.client.RestTemplate;

/**
 * An abstract implementation of a service class can connect to different TD API endpoints
 */
public abstract class Service {

    RestTemplate restTemplate;
    final static String API_URL = "https://api.tdameritrade.com/v1/marketdata";
}

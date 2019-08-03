package com.sleet.api.service;

import org.springframework.web.client.RestTemplate;

public abstract class Service {

    RestTemplate restTemplate;
    final static String API_URL = "https://api.tdameritrade.com/v1/marketdata";
}

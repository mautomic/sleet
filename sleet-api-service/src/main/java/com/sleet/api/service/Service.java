package com.sleet.api.service;

import com.sleet.api.HttpClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * An abstract implementation of a service class can connect to different TD API endpoints
 */
public abstract class Service {

    HttpClient httpClient;
    RestTemplate restTemplate;
    static final int DEFAULT_TIMEOUT_MILLIS = 5000;
    final static String API_URL = "https://api.tdameritrade.com/v1/marketdata/";

    /**
     * Class used to configure properties for a {@link RestTemplate} as an HTTP connection and read timeout can hang
     */
    protected SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(3000);
        clientHttpRequestFactory.setReadTimeout(10000);
        return clientHttpRequestFactory;
    }
}

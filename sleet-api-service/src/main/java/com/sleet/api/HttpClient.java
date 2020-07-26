package com.sleet.api;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

/**
 * Generic wrapper around {@link AsyncHttpClient} supporting sync and async GET requests
 *
 * @author mautomic
 */
public class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
    private final AsyncHttpClient client;

    public HttpClient(final int readTimeoutMillis, final int connectionTimeoutMillis) {
        client = asyncHttpClient(config().setReadTimeout(readTimeoutMillis).setConnectTimeout(connectionTimeoutMillis));
    }

    /**
     * Executes a synchronous GET request
     *
     * @param url to hit with a GET request
     * @param timeoutMillis time to wait for response before throwing an exception
     * @return A HTTP {@link Response}
     * @throws Exception if the response is not received before the timeout
     */
    public Response get(final String url, final int timeoutMillis) throws Exception {
        return get(url).get(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes an asynchronous GET request
     *
     * @param url to hit with a GET request
     * @return A {@link CompletableFuture} containing the HTTP Response
     */
    public CompletableFuture<Response> get(final String url) {
        return client.prepareGet(url).execute().toCompletableFuture();
    }

    /**
     * Executes an asynchronous GET request and then the instructions in
     * the provided {@link Handler}
     *
     * @param url to hit with a GET request
     * @param handler for instructions after response is received
     */
    public void get(final String url, final Handler<Response> handler) {
        client.prepareGet(url).execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) {
                return response;
            }
        }).toCompletableFuture().whenComplete((response, exception) -> {
            if (exception == null)
                handler.handle(response);
            else
                LOG.error("Error handling response: {}", exception.getMessage());
        });
    }
}

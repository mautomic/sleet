package com.sleet.api;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

/**
 * Generic wrapper around {@link AsyncHttpClient} supporting sync and async GET/POST requests
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
     * @param url           to hit with a GET request
     * @param headerParams  to add to request
     * @param timeoutMillis time to wait for response before throwing an exception
     * @return A HTTP {@link Response}
     * @throws Exception if the response is not received before the timeout
     */
    public Response get(final String url, final Map<String, String> headerParams, final int timeoutMillis) throws Exception {
        return get(url, headerParams).get(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes an asynchronous GET request
     *
     * @param url          to hit with a GET request
     * @param headerParams to add to request
     * @return A {@link CompletableFuture} containing the HTTP Response
     */
    public CompletableFuture<Response> get(final String url, Map<String, String> headerParams) {
        final Request request = createGetRequest(url, headerParams);
        return client.executeRequest(request).toCompletableFuture();
    }

    /**
     * Executes an asynchronous GET request and then the instructions in
     * the provided {@link Handler}
     *
     * @param url          to hit with a GET request
     * @param headerParams to add to request
     * @param handler      for instructions after response is received
     */
    public void get(final String url, Map<String, String> headerParams, final Handler<Response> handler) {
        final Request request = createGetRequest(url, headerParams);
        client.executeRequest(request, new AsyncCompletionHandler<Response>() {
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

    /**
     * Executes a synchronous POST request
     *
     * @param url           to send a POST request
     * @param body          to send in request
     * @param headerParams  to add to request
     * @param timeoutMillis time to wait for response before throwing an exception
     * @return A HTTP {@link Response}
     * @throws Exception if the response is not received before the timeout
     */
    public Response post(final String url, final String body, Map<String, String> headerParams, final int timeoutMillis) throws Exception {
        return post(url, body, headerParams).get(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes an asynchronous POST request
     *
     * @param url          to send a POST request
     * @param body         to send in request
     * @param headerParams to add to request
     * @return A {@link CompletableFuture} containing the HTTP Response
     */
    public CompletableFuture<Response> post(final String url, final String body, Map<String, String> headerParams) {
        final Request request = createPostRequest(url, body, headerParams);
        final ListenableFuture<Response> listenableFuture = client.executeRequest(request);
        return listenableFuture.toCompletableFuture();
    }

    /**
     * Executes an asynchronous POST request and then the instructions in
     * the provided {@link Handler}
     *
     * @param url          to send a POST request
     * @param body         to send in request
     * @param headerParams to add to request
     * @param handler      for instructions after response is received
     */
    public void post(final String url, final String body, Map<String, String> headerParams, final Handler<Response> handler) {
        final Request request = createPostRequest(url, body, headerParams);
        final ListenableFuture<Response> listenableFuture = client.executeRequest(request);
        listenableFuture.toCompletableFuture().whenComplete((response, exception) -> {
            if (exception == null)
                handler.handle(response);
            else
                LOG.error("Error handling response: {}", exception.getMessage());
        });
    }

    /**
     * Create a {@link Request} for GET methods
     *
     * @param url          to send a GET request
     * @param headerParams to add to request
     * @return a {@link Request} to fire with the http client
     */
    private Request createGetRequest(final String url, final Map<String, String> headerParams) {
        final RequestBuilder requestBuilder = new RequestBuilder("GET")
                .setUrl(url);
        if (headerParams != null)
            headerParams.forEach(requestBuilder::setHeader);
        return requestBuilder.build();
    }

    /**
     * Create a {@link Request} for POST methods
     *
     * @param url          to send a POST request
     * @param body         to send in request
     * @param headerParams to add to request
     * @return a {@link Request} to fire with the http client
     */
    private Request createPostRequest(final String url, final String body, final Map<String, String> headerParams) {
        final RequestBuilder requestBuilder = new RequestBuilder("POST")
                .setUrl(url)
                .setBody(body);
        if (headerParams != null)
            headerParams.forEach(requestBuilder::setHeader);
        return requestBuilder.build();
    }
}

package com.sleet.api;

import org.asynchttpclient.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class HttpClientTest {

    @Test
    public void testGetSync() throws Exception {
        String url = "https://httpbin.org/get";
        HttpClient client = new HttpClient(5000, 5000);

        Response response = client.get(url, 2000);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertTrue(response.getResponseBody().length() > 0);
        Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
    }

    @Test
    public void testGetAsync() throws Exception {
        String url = "https://httpbin.org/get";
        HttpClient client = new HttpClient(5000, 5000);

        CompletableFuture<Response> responseFuture = client.get(url);
        Assert.assertNotNull(responseFuture);

        Response response = responseFuture.get(10, TimeUnit.SECONDS);
        Assert.assertTrue(response.getResponseBody().length() > 0);
        Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
    }

    @Test
    public void testGetAsyncWithHandler() {
        String url = "https://httpbin.org/get";
        HttpClient client = new HttpClient(5000, 5000);

        Handler<Response> handler = response -> {
            Assert.assertTrue(response.getResponseBody().length() > 0);
            Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
        };

        client.get(url, handler);
    }
}

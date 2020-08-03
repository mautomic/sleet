package com.sleet.api;

import org.asynchttpclient.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test class for {@link HttpClient}
 *
 * @author mautomic
 */
public class HttpClientTest {

    static HttpClient client;

    @BeforeClass
    public static void setup() {
        client = new HttpClient(5000, 5000);
    }

    @Test
    public void testGetSync() throws Exception {
        String url = "https://httpbin.org/get";
        Response response = client.get(url, 2000);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertTrue(response.getResponseBody().length() > 0);
        Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
    }

    @Test
    public void testGetAsync() throws Exception {
        String url = "https://httpbin.org/get";
        CompletableFuture<Response> responseFuture = client.get(url);
        Assert.assertNotNull(responseFuture);

        Response response = responseFuture.get(10, TimeUnit.SECONDS);
        Assert.assertTrue(response.getResponseBody().length() > 0);
        Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
    }

    @Test
    public void testGetAsyncWithHandler() {
        String url = "https://httpbin.org/get";
        Handler<Response> handler = response -> {
            Assert.assertTrue(response.getResponseBody().length() > 0);
            Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
        };
        client.get(url, handler);
    }

    @Test
    public void testPostSync() throws Exception {
        String url = "https://httpbin.org/post";
        String body = "key=value";
        Response response = client.post(url, body, 2000);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertTrue(response.getResponseBody().length() > 0);
        Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
    }

    @Test
    public void testPostAsync() throws Exception {
        String url = "https://httpbin.org/post";
        String body = "key=value";
        CompletableFuture<Response> responseFuture = client.post(url, body);
        Assert.assertNotNull(responseFuture);

        Response response = responseFuture.get(10, TimeUnit.SECONDS);
        Assert.assertTrue(response.getResponseBody().length() > 0);
        Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
    }

    @Test
    public void testPostAsyncWithHandler() {
        String url = "https://httpbin.org/post";
        String body = "key=value";
        Handler<Response> handler = response -> {
            Assert.assertTrue(response.getResponseBody().length() > 0);
            Assert.assertTrue(response.getResponseBody().contains("httpbin.org"));
        };
        client.post(url, body, handler);
    }
}

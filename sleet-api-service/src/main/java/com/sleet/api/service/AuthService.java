package com.sleet.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleet.api.RequestUtil;
import com.sleet.api.model.Token;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.sleet.api.Constants.*;

/**
 * An API interface that provides a method to retrieve a
 * {@link Token} for order interactions and account information
 *
 * @author mautomic
 */
public class AuthService {

    private final String clientId;
    private final String redirectUri;
    private final AsyncHttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    public AuthService(final String clientId, final String redirectUri, final AsyncHttpClient httpClient) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.httpClient = httpClient;
    }

    /**
     * Retrieve a {@link Token} containing a new access token and a refresh token from
     * the TD API
     *
     * @param code           current access or refresh token code
     * @param isRefreshToken designates to use access token or refresh token field in POST
     * @return {@link} Token with new access and refresh tokens
     * @throws Exception if there is an issue with the POST request
     */
    public Token getPostAccessToken(final String code, final boolean isRefreshToken) throws Exception {

        final StringBuilder builder = new StringBuilder();
        builder.append(CLIENT_ID).append(EQUALS).append(clientId).append(AND);
        builder.append(REDIRECT_URI).append(EQUALS).append(redirectUri).append(AND);

        if (isRefreshToken) {
            builder.append(GRANT_TYPE).append(EQUALS).append(REFRESH_TOKEN).append(AND);
            builder.append(REFRESH_TOKEN).append(EQUALS).append(code);
        } else {
            builder.append(ACCESS_TYPE).append(EQUALS).append(OFFLINE).append(AND);
            builder.append(GRANT_TYPE).append(EQUALS).append(AUTHORIZATION_CODE).append(AND);
            builder.append(CODE).append(EQUALS).append(code);
        }

        final Map<String, String> headerParams = new HashMap<>();
        headerParams.put(CONTENT_TYPE, URL_ENCODED);

        final String url = API_URL + TOKEN_ENDPOINT;
        final Request request = RequestUtil.createPostRequest(url, builder.toString(), headerParams);
        final Response response = httpClient.executeRequest(request).get(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        return deserializeResponse(response);
    }

    /**
     * Deserialize the response body into a {@link Token}
     *
     * @param response of the POST request to TD
     * @return a {@link Token} with new access and refresh tokens
     * @throws Exception if there is an issue with deserialization
     */
    private Token deserializeResponse(final Response response) throws Exception {
        final String responseBody = response.getResponseBody();
        return mapper.readValue(responseBody, Token.class);
    }
}

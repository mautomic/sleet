package com.sleet.api.service;

import com.sleet.api.HttpClient;
import com.sleet.api.model.Token;
import org.asynchttpclient.Response;

/**
 * A {@link Service} implementation that provides a method to retrieve a
 * {@link Token} for order interactions and account information
 *
 * @author mautomic
 */
public class AuthService extends Service {

    private final static String TOKEN_ENDPOINT = "oauth2/token";
    private final static String GRANT_TYPE = "grant_type";
    private final static String AUTHORIZATION_CODE = "authorization_code";
    private final static String REFRESH_TOKEN = "refresh_token";
    private final static String CLIENT_ID = "client_id";
    private final static String REDIRECT_URI = "redirect_uri";
    private final static String ACCESS_TYPE = "access_type";
    private final static String CODE = "code";
    private final static String OFFLINE = "offline";
    private final String clientId;
    private final String redirectUri;

    public AuthService(final String clientId, final String redirectUri) {
        httpClient = new HttpClient(DEFAULT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS);
        this.clientId = clientId;
        this.redirectUri = redirectUri;
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
        builder.append(CLIENT_ID).append("=").append(clientId).append("&");
        builder.append(REDIRECT_URI).append("=").append(redirectUri).append("&");
        builder.append(ACCESS_TYPE).append("=").append(OFFLINE).append("&");

        if (isRefreshToken) {
            builder.append(GRANT_TYPE).append("=").append(REFRESH_TOKEN).append("&");
            builder.append(REFRESH_TOKEN).append("=").append(code);
        } else {
            builder.append(GRANT_TYPE).append("=").append(AUTHORIZATION_CODE).append("&");
            builder.append(CODE).append("=").append(code);
        }

        final String url = API_URL + TOKEN_ENDPOINT;
        final Response response = httpClient.post(url, builder.toString(), null, DEFAULT_TIMEOUT_MILLIS);
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

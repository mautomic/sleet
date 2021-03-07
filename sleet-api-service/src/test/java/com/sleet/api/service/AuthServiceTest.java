package com.sleet.api.service;

import com.sleet.api.HttpClient;
import com.sleet.api.model.Token;
import org.junit.Assert;
import org.junit.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Test class for {@link AuthService}
 *
 * @author mautomic
 */
public class AuthServiceTest {

    @Test
    public void testGetAccessTokenWithAuthGrant() throws Exception {
        final String redirectUri = "https://127.0.0.1";
        final String grantCode = "";

        final AuthService authService = new AuthService(TestConstants.API_KEY, redirectUri,
                new HttpClient(5000, 5000));
        final Token token = authService.getPostAccessToken(grantCode, false);
        System.out.println(token.getAccessToken());
        System.out.println(token.getRefreshToken());

        Assert.assertNotNull(token);
        Assert.assertNotNull(token.getAccessToken());
        Assert.assertNotNull(token.getRefreshToken());
    }

    @Test
    public void testGetAccessTokenWithRefreshToken() throws Exception {
        final String redirectUri = "https://127.0.0.1";
        final String refreshToken = "";

        final String refreshTokenEncoded = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8.toString());

        final AuthService authService = new AuthService(TestConstants.API_KEY, redirectUri,
                new HttpClient(5000, 5000));
        final Token token = authService.getPostAccessToken(refreshTokenEncoded, true);
        System.out.println(token.getAccessToken());
        System.out.println(token.getRefreshToken());

        Assert.assertNotNull(token);
        Assert.assertNull(token.getAccessToken());
        Assert.assertNotNull(token.getRefreshToken());
    }
}

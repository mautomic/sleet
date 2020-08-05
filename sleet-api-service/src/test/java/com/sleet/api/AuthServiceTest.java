package com.sleet.api;

import com.sleet.api.model.Token;
import com.sleet.api.service.AuthService;
import org.junit.Assert;
import org.junit.Test;

public class AuthServiceTest {

    @Test
    public void testOptionChainRequest() throws Exception {

        // Must supply API key and grant code for TD API in order to run test. See readme for info.
        final String apiKey = "";
        final String redirectUri = "https://127.0.0.1";
        final String grant_code = "";

        final AuthService authService = new AuthService(apiKey, redirectUri);
        final Token token = authService.getPostAccessToken(grant_code, false);
        System.out.println(token.getAccessToken());
        System.out.println(token.getRefreshToken());

        Assert.assertNotNull(token);
    }
}

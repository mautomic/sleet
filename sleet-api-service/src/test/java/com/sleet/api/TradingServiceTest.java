package com.sleet.api;

import com.sleet.api.service.TradingService;
import org.junit.Assert;
import org.junit.Test;

public class TradingServiceTest {

    @Test
    public void testAccountInfoRequest() throws Exception {

        // Must supply account num and access token for TD API in order to run test. See readme for info.
        final String accessToken = "";
        final String accountNum = "";

        final TradingService tradingService = new TradingService();
        final String jsonAccountInfo = tradingService.getAccountInfo(accountNum, accessToken);
        System.out.println(jsonAccountInfo);
        Assert.assertNotNull(jsonAccountInfo);
    }
}

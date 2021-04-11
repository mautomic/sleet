package com.sleet.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.sleet.api.util.RequestUtil
import org.asynchttpclient.Dsl
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import kotlin.Throws
import java.lang.Exception
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Test class for [AuthService]
 *
 * @author mautomic
 */
class AuthServiceTest {

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testGetAccessTokenWithAuthGrant() {
        // This grantCode needs to be URL encoded
        val grantCode = ""
        val redirectUri = "https://127.0.0.1:8443/callback"

        val authService = AuthService(TestConstants.API_KEY, redirectUri, Dsl.asyncHttpClient(Dsl.config()))
        val token = authService.getPostAccessToken(grantCode, false)
        println(token?.accessToken)
        println(token?.refreshToken)
        Assert.assertNotNull(token)
        Assert.assertNotNull(token?.accessToken)
        Assert.assertNotNull(token?.refreshToken)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testGetAccessTokenWithRefreshToken() {
        // This refreshToken needs to be URL encoded
        val refreshToken = ""
        val redirectUri = "https://127.0.0.1:8443/callback"

        val refreshTokenEncoded = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8.toString())
        val authService = AuthService(TestConstants.API_KEY, redirectUri, Dsl.asyncHttpClient(Dsl.config()))
        val token = authService.getPostAccessToken(refreshTokenEncoded, true)
        println(token?.accessToken)
        println(token?.refreshToken)
        Assert.assertNotNull(token)
        Assert.assertNull(token?.accessToken)
        Assert.assertNotNull(token?.refreshToken)
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testGetUserPrincipals() {
        val redirectUri = "https://127.0.0.1:8443/callback"
        val authService = AuthService(TestConstants.API_KEY, redirectUri, Dsl.asyncHttpClient(Dsl.config()))

        val userPrincipals = authService.getUserPrincipals(TestConstants.ACCESS_TOKEN)
        Assert.assertNotNull(userPrincipals)
        Assert.assertNotNull(userPrincipals?.streamerInfo)
        Assert.assertNotNull(userPrincipals?.streamerSubscriptionKeys)
        Assert.assertNotNull(userPrincipals?.accounts)
        Assert.assertFalse(userPrincipals?.accounts!!.isEmpty())
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun testStreaming() {
        val redirectUri = "https://127.0.0.1:8443/callback"
        val authService = AuthService(TestConstants.API_KEY, redirectUri, Dsl.asyncHttpClient(Dsl.config()))

        val userPrincipals = authService.getUserPrincipals(TestConstants.ACCESS_TOKEN)
        val loginPayload = RequestUtil.createStreamingLoginPayload(userPrincipals!!)
        println("Sent to ws: " + loginPayload)

        val c = ExampleStreamingClient(URI("wss://streamer-ws.tdameritrade.com/ws"), loginPayload)
        c.connect()

        // Wait for a few seconds so we can connect before sending a quote request
        Thread.sleep(3000)

        val request = mapOf("service" to "QUOTE",
            "requestid" to "service_count",
            "command" to "SUBS",
            "account" to TestConstants.ACCOUNT_NUM,
            "source" to TestConstants.API_KEY,
            "parameters" to mapOf<String, Any>(
                "keys" to "AAPL",
                "fields" to "0,1,2,3,4,5,6,7,8,9"
            )
        )

        val requestString = ObjectMapper().writeValueAsString(request)
        c.send(requestString)

        var x = 0
        while (x < 5) {
            Thread.sleep(1000)
            x++
        }
    }
}
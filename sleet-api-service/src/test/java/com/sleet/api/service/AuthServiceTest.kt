package com.sleet.api.service

import org.asynchttpclient.Dsl
import org.junit.Assert
import org.junit.Test
import kotlin.Throws
import java.lang.Exception
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Test class for [AuthService]
 *
 * @author mautomic
 */
class AuthServiceTest {

    @Test
    @Throws(Exception::class)
    fun testGetAccessTokenWithAuthGrant() {
        val redirectUri = "https://127.0.0.1"
        val grantCode = ""
        val authService = AuthService(TestConstants.API_KEY, redirectUri, Dsl.asyncHttpClient(Dsl.config()))
        val token = authService.getPostAccessToken(grantCode, false)
        println(token.accessToken)
        println(token.refreshToken)
        Assert.assertNotNull(token)
        Assert.assertNotNull(token.accessToken)
        Assert.assertNotNull(token.refreshToken)
    }

    @Test
    @Throws(Exception::class)
    fun testGetAccessTokenWithRefreshToken() {
        val redirectUri = "https://127.0.0.1"
        val refreshToken = ""
        val refreshTokenEncoded = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8.toString())
        val authService = AuthService(TestConstants.API_KEY, redirectUri, Dsl.asyncHttpClient(Dsl.config()))
        val token = authService.getPostAccessToken(refreshTokenEncoded, true)
        println(token.accessToken)
        println(token.refreshToken)
        Assert.assertNotNull(token)
        Assert.assertNull(token.accessToken)
        Assert.assertNotNull(token.refreshToken)
    }
}
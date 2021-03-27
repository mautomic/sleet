package com.sleet.api.service

import org.asynchttpclient.Dsl
import org.junit.Assert
import org.junit.Ignore
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
}
package com.sleet.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.sleet.api.Constants
import com.sleet.api.util.RequestUtil.Companion.createPostRequest
import com.sleet.api.model.Token
import com.sleet.api.model.UserPrincipals
import com.sleet.api.util.RequestUtil
import org.asynchttpclient.AsyncHttpClient

import kotlin.Throws
import java.lang.Exception
import java.util.Base64
import java.util.concurrent.TimeUnit

/**
 * An API interface that provides OAUTH [Token] retrieval for order interactions
 * and account information
 *
 * @author mautomic
 */
class AuthService(
    private val appKey: String,
    private val appSecret: String,
    private val redirectUri: String,
    private val httpClient: AsyncHttpClient,
) {
    private var token: Token = Token()

    companion object {
        private val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    }

    fun setToken(token: Token) {
        this.token = token
    }

    fun getToken(): Token {
        return this.token
    }

    /**
     * Retrieve a [Token] containing a new access token and a refresh token from the Schwab API
     *
     * @param code           current refresh token code
     * @return [Token] with new access and refresh tokens
     * @throws Exception if there is an issue with the POST request
     */
    @Throws(Exception::class)
    fun getUpdatedAccessToken(code: String): Token? {

        val credentials = "$appKey:$appSecret"
        val base64Credentials = Base64.getEncoder().encodeToString(credentials.toByteArray())
        val authorization = "Basic $base64Credentials"

        val headerParams: Map<String, String> = mapOf(
            Constants.CONTENT_TYPE to Constants.URL_ENCODED,
            Constants.AUTHORIZATION to authorization
        )

        val payload = "grant_type=refresh_token&refresh_token=$code"
        val url: String = Constants.API_URL + Constants.TOKEN_ENDPOINT
        val request = createPostRequest(url, payload, headerParams)
        val response = httpClient.executeRequest(request)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]

        if (response.statusCode != 200)
            return null
        return mapper.readValue(response.responseBody, Token::class.java)
    }

    /**
     * Get a [UserPrincipals] payload from the Schwab API for streaming setup
     *
     * @param accessToken used to authenticate with TD
     * @return [UserPrincipals] with account and streaming details
     * @throws Exception if there is an issue with the GET request
     */
    @Throws(Exception::class)
    fun getUserPrincipals(accessToken: String): UserPrincipals? {
        val url = Constants.API_URL + Constants.QUERY_PARAM_USER_PRINCIPALS
        val headerMap: Map<String, String> = mapOf(
            Constants.AUTHORIZATION to Constants.BEARER + accessToken
        )
        val request = RequestUtil.createGetRequest(url, headerMap)
        val response = httpClient.executeRequest(request)[Constants.DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS]
        if (response.statusCode != 200)
            return null
        return mapper.readValue(response.responseBody, UserPrincipals::class.java)
    }
}
package com.sleet.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.sleet.api.Constants
import com.sleet.api.Constants.AND
import com.sleet.api.Constants.EQUALS
import com.sleet.api.util.RequestUtil.Companion.createPostRequest
import com.sleet.api.model.Token
import com.sleet.api.model.UserPrincipals
import com.sleet.api.util.RequestUtil
import org.asynchttpclient.AsyncHttpClient

import kotlin.Throws
import java.lang.Exception
import java.lang.StringBuilder
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 * An API interface that provides OAUTH [Token] retrieval for order interactions
 * and account information
 *
 * @author mautomic
 */
class AuthService(
    private val clientId: String,
    private val redirectUri: String,
    private val httpClient: AsyncHttpClient
) {

    companion object {
        private val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    }

    /**
     * Retrieve a [Token] containing a new access token and a refresh token from the Schwab API
     *
     * @param code           current access or refresh token code
     * @param isRefreshToken designates to use access token or refresh token field in POST
     * @return [Token] with new access and refresh tokens
     * @throws Exception if there is an issue with the POST request
     */
    @Throws(Exception::class)
    fun getPostAccessToken(code: String, isRefreshToken: Boolean): Token? {
        val builder = StringBuilder()
        builder.append(Constants.CLIENT_ID).append(EQUALS).append(clientId).append(AND)
        builder.append(Constants.REDIRECT_URI).append(EQUALS).append(redirectUri).append(AND)

        if (isRefreshToken) {
            builder.append(Constants.GRANT_TYPE).append(EQUALS).append(Constants.REFRESH_TOKEN).append(AND)
            builder.append(Constants.REFRESH_TOKEN).append(EQUALS).append(code)
        } else {
            builder.append(Constants.ACCESS_TYPE).append(EQUALS).append(Constants.OFFLINE).append(AND)
            builder.append(Constants.GRANT_TYPE).append(EQUALS).append(Constants.AUTHORIZATION_CODE).append(AND)
            builder.append(Constants.CODE).append(EQUALS).append(code)
        }

        val headerParams: MutableMap<String, String> = HashMap()
        headerParams[Constants.CONTENT_TYPE] = Constants.URL_ENCODED
        val url: String = Constants.API_URL + Constants.TOKEN_ENDPOINT

        val request = createPostRequest(url, builder.toString(), headerParams)
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
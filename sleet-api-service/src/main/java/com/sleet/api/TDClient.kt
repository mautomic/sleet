package com.sleet.api

import com.sleet.api.Constants.DEFAULT_REDIRECT_URI
import com.sleet.api.Constants.DEFAULT_TIMEOUT_MILLIS
import com.sleet.api.service.AuthService
import com.sleet.api.service.QuoteService
import com.sleet.api.service.TradingService

/**
 * Client interface to TD Ameritrade's API with sub-services grouped by common functionalities
 *
 * @author mautomic
 */
class TDClient @JvmOverloads constructor(apiKey: String, redirectUri: String = DEFAULT_REDIRECT_URI) {

    private val httpClient = HttpClient(DEFAULT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT_MILLIS)

    val authenticator: AuthService = AuthService(apiKey, redirectUri, httpClient)
    val trader: TradingService = TradingService(httpClient)
    val quoter: QuoteService = QuoteService(apiKey, httpClient)
}
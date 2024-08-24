package com.sleet.api

/**
 * Reusable variables across application
 *
 * @author mautomic
 */
object Constants {
    const val ACCESS_TYPE = "access_type"
    const val ACCOUNTS = "accounts"
    const val AND = '&'
    const val API_URL = "https://api.schwabapi.com/"
    const val APPLICATION_JSON = "application/json"
    const val AUTHORIZATION = "authorization"
    const val AUTHORIZATION_CODE = "authorization_code"
    const val BEARER = "Bearer "
    const val CLIENT_ID = "client_id"
    const val CODE = "code"
    const val CONTENT_TYPE = "Content-Type"
    const val DEFAULT_TIMEOUT_MILLIS = 5000L
    const val DEFAULT_STRIKE_COUNT = "100"
    const val DEFAULT_REDIRECT_URI = "https://127.0.0.1:8443/callback"
    const val EQUALS = '='
    const val GRANT_TYPE = "grant_type"
    const val MARKETDATA = "marketdata"
    const val PRICEHISTORY = "pricehistory"
    const val OFFLINE = "offline"
    const val ORDERS = "orders"
    const val REDIRECT_URI = "redirect_uri"
    const val REFRESH_TOKEN = "refresh_token"
    const val SAVED_ORDERS = "savedorders"
    const val SLASH = "/"
    const val TOKEN_ENDPOINT = "v1/oauth/token"
    const val URL_ENCODED = "application/x-www-form-urlencoded"
    const val QUESTION_MARK = "?"

    const val QUERY_PARAM_OTM = "&range=OTM"
    const val QUERY_PARAM_CONTRACT_TYPE = "&contractType="
    const val QUERY_PARAM_SYMBOL = "&symbol="
    const val QUERY_PARAM_STRIKE_COUNT = "&strikeCount="
    const val QUERY_PARAM_STRIKE = "&strike="
    const val QUERY_PARAM_STATUS = "&status="
    const val QUERY_PARAM_FROM_DATE = "&fromDate="
    const val QUERY_PARAM_TO_DATE = "&toDate="
    const val QUERY_PARAM_FROM_ENTERED_TIME = "&fromEnteredTime="
    const val QUERY_PARAM_TO_ENTERED_TIME = "&toEnteredTime="
    const val QUERY_PARAM_ORDERS_AND_POSITIONS = "?fields=positions%2Corders"
    const val QUERY_PARAM_USER_PRINCIPALS = "userprincipals?fields=streamerSubscriptionKeys%2CstreamerConnectionInfo"
    const val QUERY_PARAM_PERIOD_TYPE = "&periodType="
    const val QUERY_PARAM_PERIOD = "&period="
    const val QUERY_PARAM_FREQUENCY_TYPE = "&frequencyType="
    const val QUERY_PARAM_FREQUENCY = "&frequency="
    const val QUERY_PARAM_SORT = "sort="
}
package com.sleet.api

import org.asynchttpclient.Request
import org.asynchttpclient.RequestBuilder

class RequestUtil {

    companion object {

        /**
         * Create a [Request] for GET methods
         *
         * @param url          to send a GET request
         * @param headerParams to add to request
         * @return a [Request] to fire with the http client
         */
        @JvmStatic
        fun createGetRequest(url: String, headerParams: Map<String, String>?): Request? {
            val requestBuilder = RequestBuilder("GET").setUrl(url)
            headerParams?.forEach { (name: String, value: String) ->
                requestBuilder.setHeader(name, value)
            }
            return requestBuilder.build()
        }

        /**
         * Create a [Request] for POST methods
         *
         * @param url          to send a POST request
         * @param body         to send in request
         * @param headerParams to add to request
         * @return a [Request] to fire with the http client
         */
        @JvmStatic
        fun createPostRequest(url: String, body: String, headerParams: Map<String, String>?): Request? {
            val requestBuilder = RequestBuilder("POST").setUrl(url).setBody(body)
            headerParams?.forEach { (name: String, value: String) ->
                requestBuilder.setHeader(name, value)
            }
            return requestBuilder.build()
        }
    }
}
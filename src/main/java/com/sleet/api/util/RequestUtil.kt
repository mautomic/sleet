package com.sleet.api.util

import org.asynchttpclient.Request
import org.asynchttpclient.RequestBuilder

/**
 * Utilities to create requests for a [AsyncHttpClient]
 *
 * @author mautomic
 */
class RequestUtil {

    companion object {

        @JvmStatic
        fun createGetRequest(url: String, headerParams: Map<String, String>?): Request? {
            val requestBuilder = RequestBuilder("GET").setUrl(url)
            headerParams?.forEach { (name: String, value: String) ->
                requestBuilder.setHeader(name, value)
            }
            return requestBuilder.build()
        }

        @JvmStatic
        fun createPostRequest(url: String, body: String, headerParams: Map<String, String>?): Request? {
            val requestBuilder = RequestBuilder("POST").setUrl(url).setBody(body)
            headerParams?.forEach { (name: String, value: String) ->
                requestBuilder.setHeader(name, value)
            }
            return requestBuilder.build()
        }

        @JvmStatic
        fun createDeleteRequest(url: String, headerParams: Map<String, String>?): Request? {
            val requestBuilder = RequestBuilder("DELETE").setUrl(url)
            headerParams?.forEach { (name: String, value: String) ->
                requestBuilder.setHeader(name, value)
            }
            return requestBuilder.build()
        }

        @JvmStatic
        fun createPutRequest(url: String, body: String, headerParams: Map<String, String>?): Request? {
            val requestBuilder = RequestBuilder("PUT").setUrl(url).setBody(body)
            headerParams?.forEach { (name: String, value: String) ->
                requestBuilder.setHeader(name, value)
            }
            return requestBuilder.build()
        }
    }
}
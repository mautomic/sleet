package com.sleet.api.util

import com.sleet.api.model.UserPrincipals
import org.asynchttpclient.Request
import org.asynchttpclient.RequestBuilder
import java.text.SimpleDateFormat

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

        @JvmStatic
        fun createStreamingLoginPayload(userPrincipals: UserPrincipals): String {
            val streamerInfo = userPrincipals.streamerInfo!!
            val mainAccount = userPrincipals.accounts!![0]

            val timestamp = streamerInfo.tokenTimestamp
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val epochMillis = sdf.parse(timestamp).time
            val epochStr = epochMillis.toString()

            return "{'requests':[{'service':'ADMIN','requestid':'0','command':'LOGIN','account':'${mainAccount.accountId}'," +
                    "'source':'${streamerInfo.appId}','parameters':{'credential':'userid=${mainAccount.accountId}&token=${streamerInfo.token}" +
                    "&company=${mainAccount.company}&segment=${mainAccount.segment}&cddomain=${mainAccount.accountCdDomainId}&usergroup=${streamerInfo.userGroup}" +
                    "&accesslevel=${streamerInfo.accessLevel}&authorized=Y&timestamp=${epochStr}&appid=${streamerInfo.appId}&acl=${streamerInfo.acl}'," +
                    "'token':'${streamerInfo.token}','version':'1.0'}}]}"
        }
    }
}
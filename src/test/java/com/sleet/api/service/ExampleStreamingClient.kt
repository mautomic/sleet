package com.sleet.api.service

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

/**
 * Example client for TD web socket streaming
 *
 * @author mautomic
 */
class ExampleStreamingClient(
    private val serverUri: URI,
    private val login: String): WebSocketClient(serverUri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        send(login)
    }
    override fun onMessage(message: String?) {
        println("Received from ws: $message")
    }
    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Connection closed: Code: $code Reason: $reason")
    }
    override fun onError(ex: Exception?) {
        ex?.printStackTrace()
    }
}
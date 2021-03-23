package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents a serializable message to communicate about status of a [TDOrder].
 * This object was created for custom trading systems communicating with each other.
 * (i.e. a trading client sending an order to an OMS, and waiting for a response).
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class TDOrderMessage(
    open val orderId: String,
    open val order: TDOrder,
    open val orderStatus: Status
)
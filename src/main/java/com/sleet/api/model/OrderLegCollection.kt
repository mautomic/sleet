package com.sleet.api.model

/**
 * Represents a collection of legs within an [TDOrder]
 *
 * @author mautomic
 */
open class OrderLegCollection(
    var instruction: String,
    var quantity: Int,
    var instrument: Instrument
)
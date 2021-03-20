package com.sleet.api.model

/**
 * Represents an instrument within an [Order]
 *
 * @author mautomic
 */
open class Instrument(
    var symbol: String,
    var assetType: String
)
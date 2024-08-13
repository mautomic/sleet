package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents an option chain for a symbol consumed from the Schwab API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class OptionChain {
    val symbol: String? = null
    val interestRate = 0.0
    val underlyingPrice = 0.0
    val volatility = 0.0
    var callExpDateMap: Map<String, Map<String, List<Asset>>>? = null
    var putExpDateMap: Map<String, Map<String, List<Asset>>>? = null
    override fun toString(): String {
        return "$symbol : $underlyingPrice"
    }
}
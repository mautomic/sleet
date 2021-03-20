package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a custom-made generic asset. This is the parent class of any tradeable object.
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Asset(
    open val symbol: String? = null,
    open val description: String? = null,
    @get:JsonAlias("bid", "bidPrice") open val bid: Double = 0.0,
    @get:JsonAlias("ask", "askPrice") open val ask: Double = 0.0,
    @get:JsonAlias("last", "lastPrice") open val last: Double = 0.0,
    open val mark: Double = 0.0,
    open val bidSize: Int = 0,
    open val askSize: Int = 0,
    open val lastSize: Int = 0,
    open val highPrice: Double = 0.0,
    open val lowPrice: Double = 0.0,
    open val openPrice: Double = 0.0,
    open val closePrice: Double = 0.0,
    open val totalVolume: Long = 0,
    open val volatility: Double = 0.0,
    open val netChange: Double = 0.0,
    open val putCall: String? = null,
    open val delta: Double = 1.0,
    open val gamma: Double = 0.0,
    open val theta: Double = 0.0,
    open val vega: Double = 0.0,
    open val openInterest: Long = 0,
    open val theoreticalOptionValue: Double = 0.0,
    open val theoreticalVolatility: Double = 0.0,
    open val strikePrice: Double = 0.0,
    open val daysToExpiration: Int = 0,
    open val percentChange: Double = 0.0,
    open val multiplier: Double = 1.0,
    open val assetType: String? = "OPTION",
    open val assetMainType: String? = "OPTION",
    open val quoteTimeInLong: Long = 0,
    open val tradeTimeInLong: Long = 0,
    @get:JsonProperty("52WkHigh") open val fiftyTwoWeekHigh: Double = 0.0,
    @get:JsonProperty("52WkLow") open val fiftyTwoWeekLow: Double = 0.0,
    open val peRatio: Double = 0.0,
    open val divAmount: Double = 0.0,
    open val divYield: Double = 0.0
)
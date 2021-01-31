package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents a single option contract consumed from the TD API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Option(
    symbol: String? = null,
    description: String? = null,
    bid: Double = 0.0,
    ask: Double = 0.0,
    last: Double = 0.0,
    mark: Double = 0.0,
    bidSize: Int = 0,
    askSize: Int = 0,
    lastSize: Int = 0,
    highPrice: Double = 0.0,
    lowPrice: Double = 0.0,
    openPrice: Double = 0.0,
    closePrice: Double = 0.0,
    totalVolume: Long = 0,
    volatility: Double = 0.0,
    netChange: Double = 0.0,
    val putCall: String? = null,
    val delta: Double = 0.0,
    val gamma: Double = 0.0,
    val theta: Double = 0.0,
    val vega: Double = 0.0,
    val openInterest: Long = 0,
    val theoreticalOptionValue: Double = 0.0,
    val theoreticalVolatility: Double = 0.0,
    val strikePrice: Double = 0.0,
    val daysToExpiration: Int = 0,
    val percentChange: Double = 0.0
) :

    Asset(
        symbol, description, bid, ask, last, mark, bidSize, askSize, lastSize,
        highPrice, lowPrice, openPrice, closePrice, totalVolume, volatility, netChange
    ) {

    override fun toString(): String {
        return "$strikePrice : $mark"
    }

    class Builder(
        var symbol: String? = null,
        var description: String? = null,
        var bid: Double = 0.0,
        var ask: Double = 0.0,
        var last: Double = 0.0,
        var mark: Double = 0.0,
        var bidSize: Int = 0,
        var askSize: Int = 0,
        var lastSize: Int = 0,
        var highPrice: Double = 0.0,
        var lowPrice: Double = 0.0,
        var openPrice: Double = 0.0,
        var closePrice: Double = 0.0,
        var totalVolume: Long = 0,
        var volatility: Double = 0.0,
        var netChange: Double = 0.0,
        var putCall: String? = null,
        var delta: Double = 0.0,
        var gamma: Double = 0.0,
        var theta: Double = 0.0,
        var vega: Double = 0.0,
        var openInterest: Long = 0,
        var theoreticalOptionValue: Double = 0.0,
        var theoreticalVolatility: Double = 0.0,
        var strikePrice: Double = 0.0,
        var daysToExpiration: Int = 0,
        var percentChange: Double = 0.0
    ) {

        fun symbol(symbol: String) = apply { this.symbol = symbol }
        fun description(description: String) = apply { this.description = description }
        fun bid(bid: Double) = apply { this.bid = bid }
        fun ask(ask: Double) = apply { this.ask = ask }
        fun last(last: Double) = apply { this.last = last }
        fun mark(mark: Double) = apply { this.mark = mark }
        fun bidSize(bidSize: Int) = apply { this.bidSize = bidSize }
        fun askSize(askSize: Int) = apply { this.askSize = askSize }
        fun lastSize(lastSize: Int) = apply { this.lastSize = lastSize }
        fun highPrice(highPrice: Double) = apply { this.highPrice = highPrice }
        fun lowPrice(lowPrice: Double) = apply { this.lowPrice = lowPrice }
        fun openPrice(openPrice: Double) = apply { this.openPrice = openPrice }
        fun closePrice(closePrice: Double) = apply { this.closePrice = closePrice }
        fun totalVolume(totalVolume: Long) = apply { this.totalVolume = totalVolume }
        fun volatility(volatility: Double) = apply { this.volatility = volatility }
        fun netChange(netChange: Double) = apply { this.netChange = netChange }
        fun putCall(putCall: String) = apply { this.putCall = putCall }
        fun delta(delta: Double) = apply { this.delta = delta }
        fun gamma(gamma: Double) = apply { this.gamma = gamma }
        fun theta(theta: Double) = apply { this.theta = theta }
        fun vega(vega: Double) = apply { this.vega = vega }
        fun openInterest(openInterest: Long) = apply { this.openInterest = openInterest }
        fun theoreticalOptionValue(theoreticalOptionValue: Double) =
            apply { this.theoreticalOptionValue = theoreticalOptionValue }

        fun theoreticalVolatility(theoreticalVolatility: Double) =
            apply { this.theoreticalVolatility = theoreticalVolatility }

        fun strikePrice(strikePrice: Double) = apply { this.strikePrice = strikePrice }
        fun daysToExpiration(daysToExpiration: Int) = apply { this.daysToExpiration = daysToExpiration }
        fun percentChange(percentChange: Double) = apply { this.percentChange = percentChange }
        fun build() = Option(
            symbol, description, bid, ask, last, mark, bidSize, askSize, lastSize, highPrice,
            lowPrice, openPrice, closePrice, totalVolume, volatility, netChange, putCall, delta, gamma, theta, vega,
            openInterest, theoreticalOptionValue, theoreticalVolatility, strikePrice, daysToExpiration, percentChange
        )
    }
}
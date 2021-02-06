package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents a single option contract consumed from the TD API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Option(
    open val symbol: String? = null,
    open val description: String? = null,
    open val bid: Double = 0.0,
    open val ask: Double = 0.0,
    open val last: Double = 0.0,
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
    open val delta: Double = 0.0,
    open val gamma: Double = 0.0,
    open val theta: Double = 0.0,
    open val vega: Double = 0.0,
    open val openInterest: Long = 0,
    open val theoreticalOptionValue: Double = 0.0,
    open val theoreticalVolatility: Double = 0.0,
    open val strikePrice: Double = 0.0,
    open val daysToExpiration: Int = 0,
    open val percentChange: Double = 0.0,
    open val multiplier: Double = 100.0
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
        var percentChange: Double = 0.0,
        var multiplier: Double = 100.0
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
        fun multiplier(multiplier: Double) = apply { this.multiplier = multiplier }
        fun build() = Option(
            symbol,
            description,
            bid,
            ask,
            last,
            mark,
            bidSize,
            askSize,
            lastSize,
            highPrice,
            lowPrice,
            openPrice,
            closePrice,
            totalVolume,
            volatility,
            netChange,
            putCall,
            delta,
            gamma,
            theta,
            vega,
            openInterest,
            theoreticalOptionValue,
            theoreticalVolatility,
            strikePrice,
            daysToExpiration,
            percentChange,
            multiplier
        )
    }
}
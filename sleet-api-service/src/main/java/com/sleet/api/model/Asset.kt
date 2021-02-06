package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonAlias

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
    open val netChange: Double = 0.0
) {

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
        var netChange: Double = 0.0
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
        fun build() = Asset(
            symbol, description, bid, ask, last, mark, bidSize, askSize, lastSize, highPrice,
            lowPrice, openPrice, closePrice, totalVolume, volatility, netChange
        )
    }
}
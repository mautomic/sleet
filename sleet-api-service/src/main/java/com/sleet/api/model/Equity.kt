package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents an equity consumed from the TD API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Equity(
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
    open val assetType: String? = null,
    open val assetMainType: String? = null,
    open val quoteTimeInLong: Long = 0,
    open val tradeTimeInLong: Long = 0,
    @get:JsonProperty("52WkHigh") open val fiftyTwoWeekHigh: Double = 0.0,
    @get:JsonProperty("52WkLow") open val fiftyTwoWeekLow: Double = 0.0,
    open val peRatio: Double = 0.0,
    open val divAmount: Double = 0.0,
    open val divYield: Double = 0.0
) :

    Asset(
        symbol, description, bid, ask, last, mark, bidSize, askSize, lastSize,
        highPrice, lowPrice, openPrice, closePrice, totalVolume, volatility, netChange
    ) {

    override fun toString(): String {
        return symbol!!
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
        var assetType: String? = null,
        var assetMainType: String? = null,
        var quoteTimeInLong: Long = 0,
        var tradeTimeInLong: Long = 0,
        var fiftyTwoWeekHigh: Double = 0.0,
        var fiftyTwoWeekLow: Double = 0.0,
        var peRatio: Double = 0.0,
        var divAmount: Double = 0.0,
        var divYield: Double = 0.0
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
        fun assetType(assetType: String) = apply { this.assetType = assetType }
        fun assetMainType(assetMainType: String) = apply { this.assetMainType = assetMainType }
        fun quoteTimeInLong(quoteTimeInLong: Long) = apply { this.quoteTimeInLong = quoteTimeInLong }
        fun tradeTimeInLong(tradeTimeInLong: Long) = apply { this.tradeTimeInLong = tradeTimeInLong }
        fun fiftyTwoWeekHigh(fiftyTwoWeekHigh: Double) = apply { this.fiftyTwoWeekHigh = fiftyTwoWeekHigh }
        fun fiftyTwoWeekLow(fiftyTwoWeekLow: Double) = apply { this.fiftyTwoWeekLow = fiftyTwoWeekLow }
        fun peRatio(peRatio: Double) = apply { this.peRatio = peRatio }
        fun divAmount(divAmount: Double) = apply { this.divAmount = divAmount }
        fun divYield(divYield: Double) = apply { this.divYield = divYield }
        fun build() = Equity(
            symbol, description, bid, ask, last, mark, bidSize, askSize, lastSize, highPrice, lowPrice,
            openPrice, closePrice, totalVolume, volatility, netChange, assetType, assetMainType, quoteTimeInLong,
            tradeTimeInLong, fiftyTwoWeekHigh, fiftyTwoWeekLow, peRatio, divAmount, divYield
        )
    }
}
package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents an equity consumed from the Schwab API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Equity(
    open val symbol: String? = null,
    open val assetMainType: String? = null,
    open val assetSubType: String? = null,
    open val quoteType: String? = null,
    open val realtime: Boolean = false,
    open val ssid: Long = 0,
    @JsonIgnoreProperties(ignoreUnknown = true)
    open val quote: Quote? = null,
    @JsonIgnoreProperties(ignoreUnknown = true)
    open val reference: Reference? = null
) {

    override fun toString(): String {
        return symbol ?: ""
    }

    class Quote(
        @get:JsonProperty("52WeekHigh") val fiftyTwoWeekHigh: Double = 0.0,
        @get:JsonProperty("52WeekLow") val fiftyTwoWeekLow: Double = 0.0,
        val askMICId: String? = null,
        val askPrice: Double = 0.0,
        val askSize: Int = 0,
        val askTime: Long = 0,
        val bidMICId: String? = null,
        val bidPrice: Double = 0.0,
        val bidSize: Int = 0,
        val bidTime: Long = 0,
        val closePrice: Double = 0.0,
        val highPrice: Double = 0.0,
        val lastMICId: String? = null,
        val lastPrice: Double = 0.0,
        val lastSize: Int = 0,
        val lowPrice: Double = 0.0,
        val mark: Double = 0.0,
        val markChange: Double = 0.0,
        val markPercentChange: Double = 0.0,
        val netChange: Double = 0.0,
        val netPercentChange: Double = 0.0,
        val openPrice: Double = 0.0,
        val postMarketChange: Double = 0.0,
        val postMarketPercentChange: Double = 0.0,
        val quoteTime: Long = 0,
        val securityStatus: String? = null,
        val totalVolume: Long = 0,
        val tradeTime: Long = 0
    )

    class Reference(
        val cusip: String? = null,
        val description: String? = null,
        val exchange: String? = null,
        val exchangeName: String? = null,
        val htbRate: Double = 0.0
    )

    class Builder(
        var symbol: String? = null,
        var assetMainType: String? = null,
        var assetSubType: String? = null,
        var quoteType: String? = null,
        var realtime: Boolean = false,
        var ssid: Long = 0,
        var quote: Quote? = null,
        var reference: Reference? = null
    ) {

        fun symbol(symbol: String) = apply { this.symbol = symbol }
        fun assetMainType(assetMainType: String) = apply { this.assetMainType = assetMainType }
        fun assetSubType(assetSubType: String) = apply { this.assetSubType = assetSubType }
        fun quoteType(quoteType: String) = apply { this.quoteType = quoteType }
        fun realtime(realtime: Boolean) = apply { this.realtime = realtime }
        fun ssid(ssid: Long) = apply { this.ssid = ssid }
        fun quote(quote: Quote) = apply { this.quote = quote }
        fun reference(reference: Reference) = apply { this.reference = reference }

        fun build() = Equity(
            symbol, assetMainType, assetSubType, quoteType, realtime, ssid, quote, reference
        )
    }
}

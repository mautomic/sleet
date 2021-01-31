package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an equity consumed from the TD API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Equity extends Asset {

    private String assetType;
    private String assetMainType;
    private long quoteTimeInLong;
    private long tradeTimeInLong;
    @JsonProperty("52WkHigh")
    private double fiftyTwoWeekHigh;
    @JsonProperty("52WkLow")
    private double fiftyTwoWeekLow;
    private double peRatio;
    private double divAmount;
    private double divYield;

    public String getAssetType() {
        return assetType;
    }

    public String getAssetMainType() {
        return assetMainType;
    }

    public long getQuoteTimeInLong() {
        return quoteTimeInLong;
    }

    public long getTradeTimeInLong() {
        return tradeTimeInLong;
    }

    public double getFiftyTwoWeekHigh() {
        return fiftyTwoWeekHigh;
    }

    public double getFiftyTwoWeekLow() {
        return fiftyTwoWeekLow;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public double getDivAmount() {
        return divAmount;
    }

    public double getDivYield() {
        return divYield;
    }

    @Override
    public String toString() {
        return getSymbol();
    }
}

package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an equity consumed from the TD API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Equity {

    private String assetType;
    private String assetMainType;
    private String symbol;
    private String description;
    private double bidPrice;
    private int bidSize;
    private double askPrice;
    private int askSize;
    private double lastPrice;
    private int lastSize;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;
    private double netChange;
    private int totalVolume;
    private long quoteTimeInLong;
    private long tradeTimeInLong;
    private double mark;
    private double volatility;
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

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public int getBidSize() {
        return bidSize;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public int getAskSize() {
        return askSize;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public int getLastSize() {
        return lastSize;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public double getNetChange() {
        return netChange;
    }

    public int getTotalVolume() {
        return totalVolume;
    }

    public long getQuoteTimeInLong() {
        return quoteTimeInLong;
    }

    public long getTradeTimeInLong() {
        return tradeTimeInLong;
    }

    public double getMark() {
        return mark;
    }

    public double getVolatility() {
        return volatility;
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

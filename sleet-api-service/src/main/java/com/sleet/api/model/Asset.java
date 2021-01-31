package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a custom-made generic asset
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset {

    private String symbol;
    private String description;
    @JsonAlias({"bid", "bidPrice"})
    private double bid;
    @JsonAlias({"ask", "askPrice"})
    private double ask;
    @JsonAlias({"last", "lastPrice"})
    private double last;
    private double mark;
    private int bidSize;
    private int askSize;
    private int lastSize;
    private double highPrice;
    private double lowPrice;
    private double openPrice;
    private double closePrice;
    private long totalVolume;
    private double volatility;
    private double netChange;

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public double getBid() {
        return bid;
    }

    public double getAsk() {
        return ask;
    }

    public double getLast() {
        return last;
    }

    public double getMark() {
        return mark;
    }

    public int getBidSize() {
        return bidSize;
    }

    public int getAskSize() {
        return askSize;
    }

    public int getLastSize() {
        return lastSize;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public long getTotalVolume() {
        return totalVolume;
    }

    public double getNetChange() {
        return netChange;
    }

    public double getVolatility() {
        return volatility;
    }
}

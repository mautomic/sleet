package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a single option contract consumed from the TD API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option {

    private String putCall;
    private String symbol;
    private String description;
    private String exchangeName;
    private double bid;
    private double ask;
    private double last;
    private double mark;
    private int bidSize;
    private int askSize;
    private int lastSize;
    private double highPrice;
    private double lowPrice;
    private double openPrice;
    private double closePrice;
    private int totalVolume;
    private double netChange;
    private double volatility;
    private double delta;
    private double gamma;
    private double theta;
    private double vega;
    private int openInterest;
    private double theoreticalOptionValue;
    private double theoreticalVolatility;
    private double strikePrice;
    private boolean inTheMoney;
    private int daysToExpiration;
    private int multiplier;
    private double percentChange;
    private double markChange;

    public String getPutCall() {
        return putCall;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public String getExchangeName() {
        return exchangeName;
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

    public int getTotalVolume() {
        return totalVolume;
    }

    public double getNetChange() {
        return netChange;
    }

    public double getVolatility() {
        return volatility;
    }

    public double getDelta() {
        return delta;
    }

    public double getGamma() {
        return gamma;
    }

    public double getTheta() {
        return theta;
    }

    public double getVega() {
        return vega;
    }

    public int getOpenInterest() {
        return openInterest;
    }

    public double getTheoreticalOptionValue() {
        return theoreticalOptionValue;
    }

    public double getTheoreticalVolatility() {
        return theoreticalVolatility;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public boolean isInTheMoney() {
        return inTheMoney;
    }

    public int getDaysToExpiration() {
        return daysToExpiration;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public double getMarkChange() {
        return markChange;
    }

    @Override
    public String toString() {
        return getStrikePrice() + " : " + getMark() ;
    }
}

package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a single option contract consumed from the TD API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option extends Asset {

    private String putCall;
    private String exchangeName;
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

    public String getExchangeName() {
        return exchangeName;
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

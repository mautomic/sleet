package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Represents an option chain for a symbol consumed from the TD API
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionChain {

    private String symbol;
    private double interestRate;
    private double underlyingPrice;
    private double volatility;
    private Map<String, Map<String, List<Option>>> callExpDateMap;
    private Map<String, Map<String, List<Option>>> putExpDateMap;

    public String getSymbol() {
        return symbol;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getUnderlyingPrice() {
        return underlyingPrice;
    }

    public double getVolatility() {
        return volatility;
    }

    public Map<String, Map<String, List<Option>>> getCallExpDateMap() {
        return callExpDateMap;
    }

    public Map<String, Map<String, List<Option>>> getPutExpDateMap() {
        return putExpDateMap;
    }

    public void setCallExpDateMap(Map<String, Map<String, List<Option>>> callExpDateMap) {
        this.callExpDateMap = callExpDateMap;
    }

    public void setPutExpDateMap(Map<String, Map<String, List<Option>>> putExpDateMap) {
        this.putExpDateMap = putExpDateMap;
    }

    @Override
    public String toString() {
        return getSymbol() + " : " + getUnderlyingPrice();
    }
}

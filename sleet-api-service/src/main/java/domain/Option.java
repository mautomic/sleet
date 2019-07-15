package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Option {

    private String putCall;
    private String symbol;
    private String description;
    private double bid;
    private double ask;
    private double last;
    private double mark;
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
    private int strikePrice;
    private boolean inTheMoney;
    private int daysToExpiration;

    public String getPutCall() {
        return putCall;
    }

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

    public int getStrikePrice() {
        return strikePrice;
    }

    public boolean isInTheMoney() {
        return inTheMoney;
    }

    public int getDaysToExpiration() {
        return daysToExpiration;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

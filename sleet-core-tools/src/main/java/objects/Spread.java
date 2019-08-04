package com.sleet.tools.objects;

import com.sleet.api.domain.Option;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Spread implements Comparable<Spread> {

    private String ticker;
    private String spread;
    private int expirationDays;
    private double price;
    private double buyingPower;
    private double roi;
    private double delta;
    private double gamma;
    private double theta;
    private double vega;
    private static final BigDecimal optionMultiplier = new BigDecimal(100);

    public Spread(Option shortLeg, Option longLeg) {

        this.spread = shortLeg.getStrikePrice() + "/" + longLeg.getStrikePrice();
        this.expirationDays = shortLeg.getDaysToExpiration();

        String tempTicker = shortLeg.getSymbol().substring(0, shortLeg.getSymbol().indexOf('_'));
        this.ticker = tempTicker.charAt(tempTicker.length()-1) == 'W' ? tempTicker.substring(0, tempTicker.length()-1) : tempTicker;

        this.price = roundDifference(shortLeg.getMark(), longLeg.getMark(), false, false);
        this.buyingPower = roundDifference(shortLeg.getStrikePrice(), longLeg.getStrikePrice(), true, true);
        this.roi = roundRoi();

        this.delta = roundDifference(shortLeg.getDelta(), longLeg.getDelta(), false, false);
        this.gamma = roundDifference(shortLeg.getGamma(), longLeg.getGamma(), false, false);
        this.vega = roundDifference(shortLeg.getVega(), longLeg.getVega(), false, false);
        this.theta = roundDifference(shortLeg.getTheta(), longLeg.getTheta(), false, false);
    }

    public String getTicker() { return ticker; }

    public String getSpread() {
        return spread;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public double getPrice() {
        return price;
    }

    public double getBuyingPower() {
        return buyingPower;
    }

    public double getRoi() {
        return roi;
    }

    public double getDelta() { return delta; }

    public double getGamma() { return gamma; }

    public double getTheta() { return theta; }

    public double getVega() { return vega; }

    public double roundRoi() {

        return new BigDecimal(this.price).multiply(optionMultiplier).divide(new BigDecimal(this.buyingPower), 3, RoundingMode.HALF_UP).doubleValue();
    }

    private double roundDifference(double d1, double d2, boolean multiplier, boolean absValue) {

        BigDecimal roundedValue = new BigDecimal(d1-d2);

        if (multiplier)
            roundedValue = roundedValue.multiply(optionMultiplier);

        if (absValue)
            roundedValue = roundedValue.abs();

        return roundedValue.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public String toString() {
        return "[ " + ticker + " " + spread + " " + expirationDays + " " + price + " " + buyingPower + " " + roi + " ]";
    }

    @Override
    public int compareTo(Spread spread) {

        if(this.getRoi() > spread.getRoi())
            return -1;
        else if(this.getRoi() < spread.getRoi())
            return 1;
        return 0;
    }
}
package com.sleet.tools.objects;

public class Spread implements Comparable<Spread> {

    private String ticker;
    private String spread;
    private int expirationDays;
    private double price;
    private double buyingPower;
    private double roi;

    public Spread(String ticker, String spread, int expirationDays, double price, double buyingPower, double roi) {

        this.ticker = ticker;
        this.spread = spread;
        this.expirationDays = expirationDays;
        this.price = price;
        this.buyingPower = buyingPower;
        this.roi = roi;
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
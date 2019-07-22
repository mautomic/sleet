package com.sleet.tools.objects;

public class Spread {

    private String spread;
    private int expirationDays;
    private double price;
    private double buyingPower;
    private double roi;

    public Spread(String spread, int expirationDays, double price, double buyingPower, double roi) {

        this.spread = spread;
        this.expirationDays = expirationDays;
        this.price = price;
        this.buyingPower = buyingPower;
        this.roi = roi;
    }

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
        return "[ " + spread + " " + expirationDays + " " + price + " " + buyingPower + " " + roi + " ]";
    }
}
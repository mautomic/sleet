package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a single order from the TD API
 *
 * @author mautomic
 */
public class Order {

    @JsonProperty("orderType")
    String orderType = "LIMIT";

    @JsonProperty("session")
    String session = "NORMAL";

    @JsonProperty("duration")
    String duration = "DAY";

    @JsonProperty("price")
    double price;

    @JsonProperty("orderStrategyType")
    String orderStrategyType = "SINGLE";

    @JsonProperty("complexOrderStrategyType")
    String complexOrderStrategyType = "NONE";

    @JsonProperty("orderLegCollection")
    List<OrderLegCollection> orderLegCollection;

    public Order() {
    }

    public Order(String orderType, double price, List<OrderLegCollection> orderLegCollection) {
        this.orderType = orderType;
        this.price = price;
        this.orderLegCollection = orderLegCollection;
    }

    public Order(String orderType, String session, String duration, double price, String orderStrategyType,
                 String complexOrderStrategyType, List<OrderLegCollection> orderLegCollection) {
        this.orderType = orderType;
        this.session = session;
        this.duration = duration;
        this.price = price;
        this.orderStrategyType = orderStrategyType;
        this.complexOrderStrategyType = complexOrderStrategyType;
        this.orderLegCollection = orderLegCollection;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderStrategyType() {
        return orderStrategyType;
    }

    public void setOrderStrategyType(String orderStrategyType) {
        this.orderStrategyType = orderStrategyType;
    }

    public String getComplexOrderStrategyType() {
        return complexOrderStrategyType;
    }

    public void setComplexOrderStrategyType(String complexOrderStrategyType) {
        this.complexOrderStrategyType = complexOrderStrategyType;
    }

    public List<OrderLegCollection> getOrderLegCollection() {
        return orderLegCollection;
    }

    public void setOrderLegCollection(List<OrderLegCollection> orderLegCollection) {
        this.orderLegCollection = orderLegCollection;
    }
}

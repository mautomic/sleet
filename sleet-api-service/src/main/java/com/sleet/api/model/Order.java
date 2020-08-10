package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("quantity")
    double quantity;

    @JsonProperty("price")
    double price;

    @JsonProperty("cancelable")
    boolean cancelable = true;

    @JsonProperty("status")
    String status;

    @JsonProperty("orderStrategyType")
    String orderStrategyType;

    @JsonProperty("orderLegCollection")
    OrderLegCollection orderLegCollection;

    public Order(String orderType, String session, String duration, double quantity, double price, boolean cancelable, String status, String orderStrategyType, OrderLegCollection orderLegCollection) {
        this.orderType = orderType;
        this.session = session;
        this.duration = duration;
        this.quantity = quantity;
        this.price = price;
        this.cancelable = cancelable;
        this.status = status;
        this.orderStrategyType = orderStrategyType;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderStrategyType() {
        return orderStrategyType;
    }

    public void setOrderStrategyType(String orderStrategyType) {
        this.orderStrategyType = orderStrategyType;
    }

    public OrderLegCollection getOrderLegCollection() {
        return orderLegCollection;
    }

    public void setOrderLegCollection(OrderLegCollection orderLegCollection) {
        this.orderLegCollection = orderLegCollection;
    }
}

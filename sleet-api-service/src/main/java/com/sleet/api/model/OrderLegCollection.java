package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a collection of legs within an {@link Order}
 *
 * @author mautomic
 */
public class OrderLegCollection {

    @JsonProperty("instruction")
    String instruction = "BUY";

    @JsonProperty("quantity")
    double quantity;

    @JsonProperty("instrument")
    Instrument instrument;

    public OrderLegCollection(String instruction, double quantity, Instrument instrument) {
        this.instruction = instruction;
        this.quantity = quantity;
        this.instrument = instrument;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }
}

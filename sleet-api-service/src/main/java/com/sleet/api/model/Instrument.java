package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an instrument within an {@link Order}
 *
 * @author mautomic
 */
public class Instrument {

    @JsonProperty("symbol")
    String symbol;

    @JsonProperty("assetType")
    String assetType;

    public Instrument(String symbol, String assetType) {
        this.symbol = symbol;
        this.assetType = assetType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}

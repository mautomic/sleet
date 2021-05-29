package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class Candles {
    @get:JsonProperty("candles") var candles: Array<PriceHistory> = emptyArray()
}
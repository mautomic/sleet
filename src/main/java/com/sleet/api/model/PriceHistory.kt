package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class PriceHistory {
    @get:JsonProperty("open") var open: Double = 0.0
    @get:JsonProperty("high") var high: Double? = 0.0
    @get:JsonProperty("low") var low: Double? = 0.0
    @get:JsonProperty("close") var close: Double? = 0.0
    @get:JsonProperty("volume") var volume: Int? = 0
    @get:JsonProperty("datetime") var datetime: String? = null
}
package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PriceHistory {
    val open: Double = 0.0
    val high: Double = 0.0
    val low: Double = 0.0
    val close: Double = 0.0
    val volume: Int = 0
    val datetime: String? = null
}
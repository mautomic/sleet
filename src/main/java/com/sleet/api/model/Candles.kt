package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Candles {
    val candles: Array<PriceHistory> = emptyArray()
}
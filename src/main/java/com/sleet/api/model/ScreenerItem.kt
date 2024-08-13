package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ScreenerItem(
    val description: String = "",
    val volume: Long = 0,
    val lastPrice: Double = 0.0,
    val netChange: Double = 0.0,
    val marketShare: Double = 0.0,
    val totalVolume: Long = 0,
    val trades: Long = 0,
    val netPercentChange: Double = 0.0,
    val symbol: String = ""
)

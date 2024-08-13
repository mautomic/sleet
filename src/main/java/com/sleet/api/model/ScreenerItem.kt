package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ScreenerItem(
    val change: Int = 0,
    val description: String? = null,
    val direction: String? = null,
    val last: Int = 0,
    val symbol: String? = null,
    val totalVolume: Long = 0
)

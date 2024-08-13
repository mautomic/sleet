package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Screeners {
    val screeners: Array<ScreenerItem> = emptyArray()
}

package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a response from the TD API for authentication purposes
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Token {
    @JsonProperty("access_token")
    var accessToken: String? = null
    @JsonProperty("refresh_token")
    var refreshToken: String? = null
}
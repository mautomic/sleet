package com.sleet.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a response from the TD API for authentication purposes
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}

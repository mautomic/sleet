package com.sleet.api.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents a UserPrincipal payload from the Schwab API. Items from this can be used
 * to setup websocket streaming.
 *
 * @author mautomic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class UserPrincipals {
    val userId: String? = null
    val userCdDomainId: String? = null
    val primaryAccountId: String? = null
    val lastLoginTime: String? = null
    val tokenExpirationTime: String? = null
    val loginTime: String? = null
    val accessLevel: String? = null
    val streamerInfo: StreamerInfo? = null
    val streamerSubscriptionKeys: StreamerSubscriptionKeys? = null
    val accounts: List<Account>? = null
}

class StreamerInfo {
    val streamerBinaryUrl: String? = null
    val streamerSocketUrl: String? = null
    val token: String? = null
    val tokenTimestamp: String? = null
    val userGroup: String? = null
    val accessLevel: String? = null
    val acl: String? = null
    val appId: String? = null
}

class StreamerSubscriptionKeys {
    val keys: List<Key>? = null
}

class Key {
    val key: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Account {
    val accountId: String? = null
    val displayName: String? = null
    val accountCdDomainId: String? = null
    val company: String? = null
    val segment: String? = null
    val acl: String? = null
}

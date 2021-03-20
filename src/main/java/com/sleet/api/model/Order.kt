package com.sleet.api.model

import java.util.Collections

/**
 * Represents a single order from the TD API
 *
 * @author mautomic
 */
open class Order(
    var orderType: String? = "LIMIT",
    var session: String? = "NORMAL",
    var duration: String? = "DAY",
    var price: Double = 0.0,
    var orderStrategyType: String? = "SINGLE",
    var complexOrderStrategyType: String? = "NONE",
    var orderLegCollection: List<OrderLegCollection>? = Collections.emptyList()
) {

    class Builder(
        var orderType: String? = "LIMIT",
        var session: String? = "NORMAL",
        var duration: String? = "DAY",
        var price: Double = 0.0,
        var orderStrategyType: String? = "SINGLE",
        var complexOrderStrategyType: String? = "NONE",
        var orderLegCollection: List<OrderLegCollection>? = Collections.emptyList()
    ) {
        fun orderType(orderType: String) = apply { this.orderType = orderType }
        fun session(session: String) = apply { this.session = session }
        fun duration(duration: String) = apply { this.duration = duration }
        fun price(price: Double) = apply { this.price = price }
        fun orderStrategyType(orderStrategyType: String) = apply { this.orderStrategyType = orderStrategyType }
        fun complexOrderStrategyType(complexOrderStrategyType: String) =
            apply { this.complexOrderStrategyType = complexOrderStrategyType }

        fun orderLegCollection(orderLegCollection: List<OrderLegCollection>) =
            apply { this.orderLegCollection = orderLegCollection }

        fun build() =
            Order(orderType, session, duration, price, orderStrategyType, complexOrderStrategyType, orderLegCollection)
    }
}
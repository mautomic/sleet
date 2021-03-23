package com.sleet.api.model

/**
 * Common enums required for creating and consuming TDAmeritrade orders
 *
 * @author mautomic
 */
enum class AssetType {
    EQUITY, OPTION, INDEX, MUTUAL_FUND, CASH_EQUIVALENT, FIXED_INCOME, CURRENCY
}

enum class ComplexOrderStrategyType {
    NONE, COVERED, VERTICAL, BACK_RATIO, CALENDAR, DIAGONAL, STRADDLE, STRANGLE, BUTTERFLY, CUSTOM
}

enum class Contract {
    CALL, PUT
}

enum class Duration {
    DAY, GOOD_TILL_CANCEL, FILL_OR_KILL
}

enum class Instruction {
    BUY, SELL, BUY_TO_COVER, SELL_SHORT, BUY_TO_OPEN, BUY_TO_CLOSE, SELL_TO_OPEN, SELL_TO_CLOSE
}

enum class OrderType {
    MARKET, STOP, LIMIT, STOP_LIMIT, TRAILING_STOP, TRAILING_STOP_LIMIT, NET_DEBIT, NET_CREDIT
}

enum class OrderStrategyType {
    SINGLE, OCO, TRIGGER
}

enum class Status {
    AWAITING_PARENT_ORDER, AWAITING_CONDITION, AWAITING_MANUAL_REVIEW, ACCEPTED, AWAITING_UR_OUT, PENDING_ACTIVATION,
    QUEUED, WORKING, REJECTED, PENDING_CANCEL, CANCELED, PENDING_REPLACE, REPLACED, FILLED, EXPIRED
}

enum class Session {
    NORMAL, AM, PM
}

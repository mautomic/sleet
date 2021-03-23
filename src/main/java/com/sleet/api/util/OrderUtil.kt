package com.sleet.api.util

import com.sleet.api.model.*
import java.lang.IllegalArgumentException

/**
 * Utilities to easily create [TDOrder]s
 *
 * @author mautomic
 */
class OrderUtil {

    companion object {

        /**
         * Create a simple equity limit order, valid until end of the current trading session.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun createEquityOrder(symbol: String, qty: Int, price: Double, instruction: Instruction): TDOrder {

            if (instruction != Instruction.BUY && instruction != Instruction.SELL)
                throw IllegalArgumentException("Instruction must be BUY or SELL")

            val equity = Instrument(symbol, AssetType.EQUITY.name)
            val orderLegCollection = OrderLegCollection(instruction.name, qty, equity)
            return TDOrder.Builder()
                .orderLegCollection(mutableListOf(orderLegCollection))
                .price(price)
                .build()
        }

        /**
         * Create a simple option limit order, valid until end of the current trading session.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun createOptionOrder(symbol: String, qty: Int, price: Double, instruction: Instruction): TDOrder {

            if (instruction != Instruction.BUY_TO_OPEN && instruction != Instruction.SELL_TO_OPEN &&
                    instruction != Instruction.BUY_TO_CLOSE && instruction != Instruction.SELL_TO_CLOSE)
                throw IllegalArgumentException(
                    "Instruction must be BUY_TO_OPEN, SELL_TO_OPEN, BUY_TO_CLOSE, or SELL_TO_CLOSE")

            val option = Instrument(symbol, AssetType.OPTION.name)
            val orderLegCollection = OrderLegCollection(instruction.name, qty, option)
            return TDOrder.Builder()
                .orderLegCollection(mutableListOf(orderLegCollection))
                .price(price)
                .build()
        }
    }
}
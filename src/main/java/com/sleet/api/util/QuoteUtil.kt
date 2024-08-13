package com.sleet.api.util

import com.sleet.api.model.Asset
import com.sleet.api.model.Contract
import com.sleet.api.model.OptionChain
import org.apache.commons.lang3.StringUtils
import java.util.ArrayList

/**
 * Utility functions useful for interacting with data sent and consumed from the Schwab API
 *
 * @author mautomic
 */
class QuoteUtil {

    companion object {

        /**
         * Takes each individual ticker as an input and normalizes it if contains invalid characters.
         * This is typically for indices as TD encodes them (ie. $SPX.X)
         *
         * @return a ticker without invalid characters
         */
        @JvmStatic
        fun normalizeTicker(ticker: String): String {
            if (ticker.startsWith("$") && ticker.endsWith(".X")) {
                val newTicker = ticker.substring(0, ticker.length - 1)
                return newTicker.replace("[^a-zA-Z ]".toRegex(), StringUtils.EMPTY)
            }
            return ticker
        }

        /**
         * Collect all options from the option chain map
         *
         * @param chain [OptionChain] containing call and put maps
         * @return list of [Asset]s
         */
        @JvmStatic
        fun collectOptions(chain: OptionChain): List<Asset> {
            val optionList: MutableList<Asset> = ArrayList()
            chain.callExpDateMap!!.values.forEach { strikes: Map<String, List<Asset>> ->
                strikes.values.forEach {
                        options: List<Asset> -> optionList.addAll(options)
                }
            }
            chain.putExpDateMap!!.values.forEach { strikes: Map<String, List<Asset>> ->
                strikes.values.forEach {
                        options: List<Asset> -> optionList.addAll(options)
                }
            }
            return optionList
        }

        /**
         * Flatten the map structure of [OptionChain]s into a standard map
         *
         * @param chain the [OptionChain]
         * @return map of symbols and their associated [Asset]s
         */
        @JvmStatic
        fun flattenOptionChain(chain: OptionChain): MutableMap<String?, Asset> {
            val optionMap: MutableMap<String?, Asset> = HashMap()
            chain.callExpDateMap!!.values.forEach { strikes: Map<String, List<Asset>> ->
                strikes.values.forEach {
                        options: List<Asset> -> optionMap[options[0].symbol] = options[0]
                }
            }
            chain.putExpDateMap!!.values.forEach { strikes: Map<String, List<Asset>> ->
                strikes.values.forEach {
                        options: List<Asset> -> optionMap[options[0].symbol] = options[0]
                }
            }
            return optionMap
        }

        /**
         * Parse out the ticker, contract type (call/put), strike, and expiration date from the option symbol string
         *
         * @param optionSymbol to extract data from
         * @return an array of the data
         */
        @JvmStatic
        fun parseDataFromOptionSymbol(optionSymbol: String): Array<String> {
            val items: List<String> = optionSymbol.split("_")
            val ticker = items[0]
            val strike: String
            val date: String
            val contractType: Contract

            val splitData = if (items[1].contains("C")) {
                contractType = Contract.CALL
                items[1].split("C")
            }
            else {
                contractType = Contract.PUT
                items[1].split("P")
            }

            date = splitData[0]
            strike = splitData[1]

            val updatedDate = "20" + date.substring(4, 6) + "-" + date.substring(0, 2) + "-" + date.substring(2, 4)
            return arrayOf(ticker, strike, updatedDate, contractType.name)
        }
    }
}

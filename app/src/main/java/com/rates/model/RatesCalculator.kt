package com.rates.model

import io.reactivex.rxjava3.core.Single

/**
 * describes calculation protocol for recalculating money relation
 */
interface RatesCalculator {
    /**
     * accepts rates and amount of money for calculation
     * @param rates map of rate name and ratio for base url
     * @param amount requested amount of money for recalculation
     * @return map of rate names and money in this currency
     */
    fun recalculateRatesForAmount(
        rates: Map<String, Double>,
        amount: Double
    ): Single<Map<String, String>>
}
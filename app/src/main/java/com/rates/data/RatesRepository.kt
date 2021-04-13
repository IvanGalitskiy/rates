package com.rates.data

import io.reactivex.rxjava3.core.Single

/**
 * Describes repository for accessing rates
 */
interface RatesRepository {
    /**
     * Gets rates values
     * @param baseRate base name of currency
     * @return map of currency names and values
     */
    fun getRates(baseRate: String): Single<Map<String, Double>>
}
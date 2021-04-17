package com.rates.data

import com.rates.model.GetRatesResponse
import io.reactivex.rxjava3.core.Single

/**
 * Describes repository for accessing rates
 */
interface RatesRepository {
    /**
     * Gets rates values
     * @param baseCurrency base name of currency
     * @return map of currency names and values
     */
    fun getRates(baseCurrency: String): Single<GetRatesResponse>
}
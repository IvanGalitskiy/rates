package com.rates.model

import io.reactivex.rxjava3.core.Observable

/**
 * Describes rates listening use cases
 */
interface GetRatesUseCase {
    /**
     * starts observing for rates values
     * @param baseRate for rates calculation
     * @param amount of money for calculation
     * @return map of rates names and values
     */
    fun observeRates(baseRate: String, amount: Double): Observable<Map<String, Double>>

    /**
     * notifies ongoing observing about base rate changing
     * cause recalculation of current values and will notify #observeRates with new value
     * @param newBaseRate for rates calculation
     * @param amount of money for calculation
     */
    fun onBaseRateChanged(newBaseRate: String, amount: Double)

    /**
     * notifies ongoing observing about base rate changing
     * cause recalculation of current values and will notify #observeRates with new value
     * @param amount of money for calculation
     */
    fun onRateAmountChanged(amount: Double)
}
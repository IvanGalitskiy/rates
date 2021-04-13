package com.rates.model

import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RatesCalculatorImpl @Inject constructor() : RatesCalculator {
    override fun recalculateRatesForAmount(
        rates: Map<String, Double>,
        amount: Double
    ): Single<Map<String, Double>> {
        return Single.just(rates)
    }
}
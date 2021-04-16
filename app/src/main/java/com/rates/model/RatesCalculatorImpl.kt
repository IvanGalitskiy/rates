package com.rates.model

import com.rates.utils.OpenForTesting
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

@OpenForTesting
class RatesCalculatorImpl @Inject constructor() : RatesCalculator {
    override fun recalculateRatesForAmount(
        rates: Map<String, Double>,
        amount: Double
    ): Single<Map<String, Double>> {
        return Observable.fromIterable(rates.entries)
            .filter { it.value >= 0 }
            .map {
                val newValue = it.value * amount
                it.key to newValue
            }
            .toMap({ it.first }, { it.second })
            .map { it.toMap() }
    }
}

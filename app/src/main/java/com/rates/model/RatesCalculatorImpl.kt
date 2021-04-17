package com.rates.model

import com.rates.utils.OpenForTesting
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

@OpenForTesting
class RatesCalculatorImpl @Inject constructor() : RatesCalculator {
    override fun recalculateRatesForAmount(
        rates: List<RateModel>,
        amount: Double
    ): Single<List<RateModel>> {
        return Observable.fromIterable(rates)
            .filter { it.amount >= 0 }
            .map {
                val newValue = it.amount * amount
                it.copy(amount = newValue)
            }
            .toList()
    }
}

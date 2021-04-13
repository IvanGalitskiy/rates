package com.rates.model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class GetRatesUseCaseImpl @Inject constructor(private val ratesCalculator: RatesCalculator): GetRatesUseCase {
    private val subject = BehaviorSubject.create<Map<String, Double>>()

    override fun observeRates(baseRate: String, amount: Double): Observable<Map<String, Double>> {
        return subject
    }

    override fun onBaseRateChanged(newBaseRate: String, amount: Double) {
    }

    override fun onRateAmountChanged(amount: Double) {
    }
}
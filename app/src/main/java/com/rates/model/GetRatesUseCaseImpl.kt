package com.rates.model

import com.rates.data.RatesRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetRatesUseCaseImpl @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val ratesCalculator: RatesCalculator
) : GetRatesUseCase {
    private val fullChangeSubject = BehaviorSubject.create<Long>()
    private val amountChangeSubject = BehaviorSubject.create<Long>()
    private var lastRequestedRate: String = DEFAULT_RATE_NAME
    private var lastRequestedAmount: Double = DEFAULT_RATE_AMOUNT
    private var lastReturnedRates = mapOf<String, Double>()

    override fun observeRates(ratesRequest: RatesRequest): Observable<Map<String, Double>> {
        lastRequestedRate = ratesRequest.baseRate
        lastRequestedAmount = ratesRequest.amount

        return Observable.interval(0, 1, TimeUnit.SECONDS)
            .mergeWith(fullChangeSubject)
            .map { RatesRequest(lastRequestedRate, lastRequestedAmount) }
            .flatMapSingle {
                ratesRepository.getRates(it.baseRate)
                    .doOnSuccess { rates -> lastReturnedRates = rates }
            }
            .mergeWith(recalculateAmount())
            .flatMapSingle { ratesCalculator.recalculateRatesForAmount(it, lastRequestedAmount) }
    }

    private fun recalculateAmount() =
        amountChangeSubject
            .map { lastReturnedRates }


    override fun onBaseRateChanged(newBaseRate: String, amount: Double) {
        lastRequestedRate = newBaseRate
        lastRequestedAmount = amount
        fullChangeSubject.onNext(0)
    }

    override fun onRateAmountChanged(amount: Double) {
        lastRequestedAmount = amount
        amountChangeSubject.onNext(0)
    }

    companion object {
        private const val DEFAULT_RATE_NAME = "EUR"
        private const val DEFAULT_RATE_AMOUNT = 0.0
    }
}
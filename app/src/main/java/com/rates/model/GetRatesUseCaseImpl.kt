package com.rates.model

import com.rates.data.RatesRepository
import com.rates.errors.DefaultValuesAreNotInitializedException
import com.rates.errors.NoLocalDataFoundException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetRatesUseCaseImpl(
    private val ratesRepository: RatesRepository,
    private val ratesCalculator: RatesCalculator,
    private val requestIntervalInMillis: Long
) : GetRatesUseCase {

    private val fullChangeSubject = PublishSubject.create<Long>()
    private val amountChangeSubject = PublishSubject.create<Long>()
    private lateinit var lastRequestedRate: String
    private var lastRequestedAmount: Double = 0.0
    private var lastReturnedResponse: GetRatesResponse? = null

    @Inject
    constructor(ratesRepository: RatesRepository, ratesCalculator: RatesCalculator) : this(
        ratesRepository,
        ratesCalculator,
        REQUEST_INTERVAL_IN_MILLIS
    )

    override fun observeRates(ratesRequest: RatesRequest?): Observable<GetRatesResponse> {
        return if (ratesRequest == null && !this::lastRequestedRate.isInitialized) {
            Observable.error(DefaultValuesAreNotInitializedException())
        } else {
            setLastRequestedData(ratesRequest)
            Observable.interval(0, requestIntervalInMillis, TimeUnit.MILLISECONDS)
                .mergeWith(fullChangeSubject)
                .map { RatesRequest(lastRequestedRate, lastRequestedAmount) }
                .flatMapSingle { getRates(it) }
                .mergeWith(recalculateAmountTrigger())
                .flatMapSingle { recalculateAmount(it) }
                .map { checkIfLocalResultHaveData(it) }
                .map { addRequestedCurrencyToResponse(it) }

        }
    }

    override fun onBaseRateChanged(newBaseRate: String, amount: Double) {
        if (newBaseRate == lastRequestedRate) {
            onRateAmountChanged(amount)
        } else {
            lastRequestedRate = newBaseRate
            lastRequestedAmount = amount
            fullChangeSubject.onNext(0)
        }
    }

    override fun onRateAmountChanged(amount: Double) {
        lastRequestedAmount = amount
        amountChangeSubject.onNext(0)
    }

    private fun setLastRequestedData(ratesRequest: RatesRequest?) {
        if (ratesRequest != null) {
            lastRequestedRate = ratesRequest.baseRate
            lastRequestedAmount = ratesRequest.amount
        }
    }

    private fun getRates(ratesRequest: RatesRequest) =
        ratesRepository.getRates(ratesRequest.baseRate)
            .doOnSuccess { rates ->
                if (rates.exception == null) {
                    lastReturnedResponse = rates
                }
            }

    private fun recalculateAmountTrigger() =
        amountChangeSubject
            .map { lastReturnedResponse }

    private fun recalculateAmount(response: GetRatesResponse) =
        ratesCalculator.recalculateRatesForAmount(
            response.rates,
            lastRequestedAmount
        ).map {
            response.copy(rates = it)
        }

    private fun checkIfLocalResultHaveData(getRatesResponse: GetRatesResponse) =
        if (getRatesResponse.rates.isEmpty()) {
            GetRatesResponse(
                lastRequestedRate,
                lastReturnedResponse?.rates ?: emptyList(),
                NoLocalDataFoundException()
            )
        } else {
            getRatesResponse
        }

    private fun addRequestedCurrencyToResponse(getRatesResponse: GetRatesResponse): GetRatesResponse {
        val mutableRates = getRatesResponse.rates.toMutableList()
        mutableRates.add(0, RateModel(lastRequestedRate, lastRequestedAmount))
        return getRatesResponse.copy(rates = mutableRates)
    }

    companion object {
        const val REQUEST_INTERVAL_IN_MILLIS = 1000L
    }
}
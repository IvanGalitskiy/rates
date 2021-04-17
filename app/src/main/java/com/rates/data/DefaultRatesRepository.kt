package com.rates.data

import com.rates.model.GetRatesResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DefaultRatesRepository @Inject constructor(
    private val networkRatesRepository: NetworkRatesRepository,
    private val localRatesRepository: LocalRatesRepository
) : RatesRepository {
    override fun getRates(baseCurrency: String): Single<GetRatesResponse> {
        return networkRatesRepository.getRates(baseCurrency)
            .doOnSuccess { localRatesRepository.saveLastKnownRate(it) }
            .onErrorResumeNext { networkError ->
                localRatesRepository.getRates(baseCurrency)
                    .map { it.copy(exception = networkError) }
            }
    }
}
package com.rates.data

import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NetworkRatesRepository @Inject constructor(private val api: RatesApi) : RatesRepository {
    override fun getRates(baseRate: String): Single<Map<String, Double>> =
        api.getRates(baseRate)
            .map { it.rates }
}

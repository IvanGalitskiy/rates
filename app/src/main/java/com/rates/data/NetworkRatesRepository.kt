package com.rates.data

import com.rates.model.GetRatesResponse
import com.rates.model.RateModel
import com.rates.utils.OpenForTesting
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@OpenForTesting
class NetworkRatesRepository @Inject constructor(private val api: RatesApi) : RatesRepository {
    override fun getRates(baseCurrency: String): Single<GetRatesResponse> =
        api.getRates(baseCurrency)
            .map { mapRatesToModel(it.rates) }
            .map { GetRatesResponse(baseCurrency, it) }

    private fun mapRatesToModel(rate: Map<String, Double>): List<RateModel> {
        return rate.map { RateModel(it.key, it.value) }
    }
}


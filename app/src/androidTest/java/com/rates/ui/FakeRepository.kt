package com.rates.ui

import com.rates.data.RatesRepository
import com.rates.model.GetRatesResponse
import com.rates.model.RateModel
import io.reactivex.rxjava3.core.Single

class FakeRepository(var data: List<RateModel>, var exception: Throwable?): RatesRepository {
    override fun getRates(baseCurrency: String): Single<GetRatesResponse> {
        return Single.just(GetRatesResponse("EUR", data, exception))
    }
}
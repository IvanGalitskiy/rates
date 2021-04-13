package com.rates.ui.adapter

import com.rates.ui.RateUiModel
import javax.inject.Inject

class RatesToRateUiModelsAdapterImpl @Inject constructor(private val rateToRateUiModelAdapter: RateToRateUiModelAdapter) :
    RatesToRateUiModelsAdapter {
    override fun map(rates: Map<String, Double>): List<RateUiModel> {
        return rates
            .map { rateToRateUiModelAdapter.map(it.toPair()) }
    }
}
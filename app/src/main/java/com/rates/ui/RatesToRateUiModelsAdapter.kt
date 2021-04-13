package com.rates.ui

interface RatesToRateUiModelsAdapter {
    fun map(rates: Map<String, Double>): List<RateUiModel>
}
interface RateToRateUiModelAdapter{
    fun map(rate: Pair<String, Double>): RateUiModel
}
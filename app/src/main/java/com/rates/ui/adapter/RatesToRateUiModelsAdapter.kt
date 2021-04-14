package com.rates.ui.adapter

import com.rates.ui.RateUiModel

interface RatesToRateUiModelsAdapter {
    fun map(rates: Map<String, Double>): List<RateUiModel>
}
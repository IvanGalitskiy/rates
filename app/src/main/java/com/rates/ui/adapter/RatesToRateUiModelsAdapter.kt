package com.rates.ui.adapter

import com.rates.model.RateModel
import com.rates.ui.RateUiModel

interface RatesToRateUiModelsAdapter {
    fun map(rates: List<RateModel>): List<RateUiModel>
}
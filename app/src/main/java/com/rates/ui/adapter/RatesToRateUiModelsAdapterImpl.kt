package com.rates.ui.adapter

import com.rates.R
import com.rates.model.RateModel
import com.rates.ui.RateUiModel
import javax.inject.Inject

class RatesToRateUiModelsAdapterImpl @Inject constructor() :
    RatesToRateUiModelsAdapter {
    override fun map(rates: List<RateModel>): List<RateUiModel> {
        return rates
            .map {
                RateUiModel(
                    R.drawable.ic_close,
                    it.rateName,
                    "Currency name",
                    "%.2f".format(it.amount)
                )
            }
    }
}
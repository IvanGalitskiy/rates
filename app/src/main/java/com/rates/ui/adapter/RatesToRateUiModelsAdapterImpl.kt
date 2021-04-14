package com.rates.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.rates.R
import com.rates.ui.RateUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RatesToRateUiModelsAdapterImpl @Inject constructor(@ApplicationContext private val context: Context) :
    RatesToRateUiModelsAdapter {
    override fun map(rates: Map<String, Double>): List<RateUiModel> {
        return rates
            .map { map(it.toPair()) }
    }

    private fun map(rate: Pair<String, Double>): RateUiModel {
        return RateUiModel(ContextCompat.getDrawable(context, R.drawable.ic_close)!!, rate.first, "Currency name", 0.0)
    }
}
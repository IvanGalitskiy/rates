package com.rates.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.rates.R
import com.rates.ui.RateUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RateToRateUiModelAdapterImpl @Inject constructor(@ApplicationContext private val context: Context) :
    RateToRateUiModelAdapter {
    override fun map(rate: Pair<String, Double>): RateUiModel {
        return RateUiModel(ContextCompat.getDrawable(context, R.drawable.ic_close)!!, "", "", 0.0)
    }
}
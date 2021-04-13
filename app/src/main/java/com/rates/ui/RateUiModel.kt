package com.rates.ui

import android.graphics.drawable.Drawable

data class RateUiModel(
    val icon: Drawable,
    val rateName: String,
    val currencyName: String,
    val amount: Double
)
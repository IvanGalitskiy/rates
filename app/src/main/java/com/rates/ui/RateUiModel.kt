package com.rates.ui


data class RateUiModel(
    val icon: Int?,
    val rateName: String,
    val currencyName: String,
    var amount: String
)
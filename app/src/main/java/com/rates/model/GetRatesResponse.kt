package com.rates.model

data class GetRatesResponse(
    val baseCurrency: String,
    val rates: List<RateModel>,
    val exception: Throwable? = null
)
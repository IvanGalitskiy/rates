package com.rates.model

data class RatesRequest(val baseRate: String = DEFAULT_RATE_NAME, val amount: Double = DEFAULT_RATE_AMOUNT){

    companion object {
        const val DEFAULT_RATE_NAME = "EUR"
        const val DEFAULT_RATE_AMOUNT = 100.0
    }
}
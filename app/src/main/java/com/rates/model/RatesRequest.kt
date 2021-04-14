package com.rates.model

data class RatesRequest(val baseRate: String = DEFAULT_RATE_NAME, val amount: Double = DEFAULT_RATE_AMOUNT){

    companion object {
        private const val DEFAULT_RATE_NAME = "EUR"
        private const val DEFAULT_RATE_AMOUNT = 0.0
    }
}
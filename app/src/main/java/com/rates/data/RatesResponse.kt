package com.rates.data

data class RatesResponse(val baseCurrency: String, val rates: Map<String, Double>)
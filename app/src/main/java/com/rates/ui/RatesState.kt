package com.rates.ui

sealed class RatesState {
    class Error(exception: Throwable) : RatesState()
    class Success(rates: List<RateUiModel>): RatesState()
}

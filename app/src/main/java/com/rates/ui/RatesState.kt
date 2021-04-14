package com.rates.ui

sealed class RatesState {
    class Error(val exception: Throwable) : RatesState()
    class Success(val rates: List<RateUiModel>): RatesState()
}

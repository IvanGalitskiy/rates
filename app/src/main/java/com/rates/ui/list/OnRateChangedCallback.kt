package com.rates.ui.list

interface OnRateChangedCallback {
    fun onRateChanged(rateName: String, amount: Double)
    fun onUnexpectedTextEntered()
}
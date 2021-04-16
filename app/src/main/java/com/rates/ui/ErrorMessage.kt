package com.rates.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar

object ErrorMessage {
    fun showNetworkError(view: View, retryAction: View.OnClickListener?): Snackbar {
        return Snackbar.make(
            view,
            "No internet. Please, connect to it and tap on Retry",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            retryAction?.let {
                setAction("Retry", retryAction)
            }
            show()
        }
    }
}
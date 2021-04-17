package com.rates.ui

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

object ErrorMessage {
    private val displayingErrors = mutableListOf<Snackbar>()
    fun dismissAllErrors() {
        displayingErrors
            .forEach { it.dismiss() }
    }

    fun showNetworkError(view: View, retryAction: View.OnClickListener?): Snackbar {
        return Snackbar.make(
            view,
            "We are using last known data, because there is no connection",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            retryAction?.let {
                setAction("Retry", retryAction)
            }
            addDoDisplayingList(this)
            show()
        }
    }

    fun showNoDataError(view: View, retryAction: View.OnClickListener?): Snackbar {
        return Snackbar.make(
            view,
            "We don't have data for this currency. Try another one or connect to the internet",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            retryAction?.let {
                setAction("Retry", retryAction)
            }
            addDoDisplayingList(this)
            show()
        }
    }

    fun showUnexpectedTypingError(view: View): Snackbar {
        return Snackbar.make(
            view,
            "Text should contain only numbers and dot symbol",
            Snackbar.LENGTH_SHORT
        )
            .apply {
                addDoDisplayingList(this)
                show()
            }
    }

    private fun addDoDisplayingList(snackbar: Snackbar) {
        displayingErrors.add(snackbar)
        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                displayingErrors.remove(transientBottomBar)
            }
        })
    }
}
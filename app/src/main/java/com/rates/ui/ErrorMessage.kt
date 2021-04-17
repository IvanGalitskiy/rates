package com.rates.ui

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.rates.R

object ErrorMessage {
    private val displayingErrors = mutableListOf<Snackbar>()
    fun dismissAllErrors() {
        displayingErrors
            .forEach { it.dismiss() }
    }

    fun showNetworkError(view: View, retryAction: View.OnClickListener?): Snackbar {
        return Snackbar.make(
            view,
            view.context.getString(R.string.network_error),
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
            view.context.getString(R.string.missing_data_error),
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
            view.context.getString(R.string.illegal_input_error),
            Snackbar.LENGTH_SHORT
        )
            .apply {
                addDoDisplayingList(this)
                show()
            }
    }

    fun showUnexpectedError(view: View): Snackbar {
        return Snackbar.make(
            view,
            view.context.getString(R.string.unexpected_error),
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
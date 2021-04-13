package com.rates.utils

import io.reactivex.rxjava3.disposables.Disposable

fun Disposable?.disposeIfNeeded() {
    if (this != null && !isDisposed) {
        dispose()
    }
}

fun Disposable?.isActive() = this != null && !isDisposed
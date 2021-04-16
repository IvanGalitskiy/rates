package com.rates.utils

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException || this is ConnectException || this is SocketTimeoutException
}
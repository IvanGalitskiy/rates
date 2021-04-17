package com.rates.errors

open class AppExpectedError : Throwable()

class DefaultValuesAreNotInitializedException : AppExpectedError()

class NoLocalDataFoundException : AppExpectedError()
package com.rates.data

interface ClassToStringConverter<T> {
    fun convertToString(data: T): String
    fun convertToClass(string: String): T
}
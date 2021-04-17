package com.rates.data

import com.rates.model.RateModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

import javax.inject.Inject

class MoshiRatesConverter @Inject constructor() : ClassToStringConverter<List<RateModel>> {
    private val moshi = Moshi.Builder().build()

    private val adapter by lazy {
        val type: Type = Types.newParameterizedType(
            List::class.java,
            RateModel::class.java
        )

        moshi.adapter<List<RateModel>>(type)
    }

    override fun convertToString(data: List<RateModel>): String {
        return adapter.toJson(data)
    }

    override fun convertToClass(string: String): List<RateModel> {
        return adapter.fromJson(string)
            ?: throw IllegalArgumentException("Can't convert given string to list")
    }
}

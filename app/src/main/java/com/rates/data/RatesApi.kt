package com.rates.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {
    @GET("android/latest")
    fun getRates(@Query("base") base: String): Single<RatesResponse>
}
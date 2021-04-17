package com.rates

import com.rates.data.RatesApi
import com.rates.data.RatesResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ApiStub @Inject constructor() : RatesApi {
    private var requestCount: Int = 0
    override fun getRates(base: String): Single<RatesResponse> {
        return Single.fromCallable {
            // simulate receiving of different rates
            requestCount++
            if (requestCount % 2 == 0) {
                RatesResponse(
                    "_CURRENCY_0", FIRST_TESTING_VARIANT.toMap()
                )
            } else {
                RatesResponse(
                    "_CURRENCY_0", SECOND_TESTING_VARIANT.toMap()
                )
            }
        }
    }

    companion object {
        val FIRST_TESTING_VARIANT = listOf(
            "_CURRENCY_1" to 1.0,
            "_CURRENCY_2" to 2.0,
            "_CURRENCY_3" to 3.0,
            "_CURRENCY_4" to 4.0,
            "_CURRENCY_5" to 5.0,
            "_CURRENCY_6" to 6.0,
            "_CURRENCY_7" to 7.0,
            "_CURRENCY_8" to 8.0,
            "_CURRENCY_9" to 9.0,
            "_CURRENCY_10" to 10.0,
            "_CURRENCY_11" to 11.0,
            "_CURRENCY_12" to 12.0,
            "_CURRENCY_13" to 13.0,
            "_CURRENCY_14" to 14.0,
            "_CURRENCY_15" to 15.0,
            "_CURRENCY_16" to 16.0,
            "_CURRENCY_17" to 17.0
        )
        val SECOND_TESTING_VARIANT = listOf(
            "_CURRENCY_1" to 1.1,
            "_CURRENCY_2" to 2.2,
            "_CURRENCY_3" to 3.3,
            "_CURRENCY_4" to 4.4,
            "_CURRENCY_5" to 5.5,
            "_CURRENCY_6" to 6.6,
            "_CURRENCY_7" to 7.7,
            "_CURRENCY_8" to 8.8,
            "_CURRENCY_9" to 9.9,
            "_CURRENCY_10" to 10.1,
            "_CURRENCY_11" to 11.2,
            "_CURRENCY_12" to 12.3,
            "_CURRENCY_13" to 13.4,
            "_CURRENCY_14" to 14.5,
            "_CURRENCY_15" to 15.6,
            "_CURRENCY_16" to 16.7,
            "_CURRENCY_17" to 17.8
        )
    }
}
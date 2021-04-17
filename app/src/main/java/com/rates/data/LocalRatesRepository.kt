package com.rates.data

import android.content.Context
import android.content.SharedPreferences
import com.rates.model.GetRatesResponse
import com.rates.model.RateModel
import com.rates.utils.OpenForTesting
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * We will not use database, because of we don't need it
 * We just need simple key-value storage for saving last known rates
 * In case of more complicated logic or big amount of data consider to use database
 *
 * I don't know how it's possible to use not fresh currency rates, but offline mode was in requirements
 */
@OpenForTesting
class LocalRatesRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val classToStringConverter: ClassToStringConverter<List<RateModel>>
) :
    RatesRepository {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    override fun getRates(baseCurrency: String): Single<GetRatesResponse> {
        return Single.fromCallable {
            val ratesString = sharedPreferences.getString(baseCurrency, null)
            if (ratesString == null) {
                GetRatesResponse(baseCurrency, emptyList())
            } else {
                val rates = classToStringConverter.convertToClass(ratesString)
                GetRatesResponse(baseCurrency, rates)
            }
        }
    }

    fun saveLastKnownRate(ratesResponse: GetRatesResponse) {
        val ratesAsString = classToStringConverter.convertToString(ratesResponse.rates)
        sharedPreferences.edit()
            .putString(ratesResponse.baseCurrency, ratesAsString)
            .apply()
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "rates_preference"
    }
}
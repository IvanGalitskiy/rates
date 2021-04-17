package com.rates.di

import com.rates.BuildConfig
import com.rates.data.ClassToStringConverter
import com.rates.data.MoshiRatesConverter
import com.rates.data.RatesApi
import com.rates.model.GetRatesResponse
import com.rates.model.RateModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideApi(): RatesApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RatesApi::class.java)
    }

//    @Provides
//    fun ratesConverter(): ClassToStringConverter<List<RateModel>>{
//        return MoshiRatesConverter()
//    }
}
package com.rates.di

import com.rates.data.*
import com.rates.model.*
import com.rates.ui.adapter.RatesToRateUiModelsAdapter
import com.rates.ui.adapter.RatesToRateUiModelsAdapterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RatesModule {
    @Binds
    fun provideGetRatesUseCase(getRatesUseCaseImpl: GetRatesUseCaseImpl): GetRatesUseCase

    @Binds
    fun provideRatesCalculator(ratesCalculator: RatesCalculatorImpl): RatesCalculator

    @Binds
    fun provideRatesToRateUiModelsAdapter(ratesToRateUiModelsAdapterImpl: RatesToRateUiModelsAdapterImpl): RatesToRateUiModelsAdapter

    @Binds
    fun provideRatesRepository(ratesRepository: DefaultRatesRepository): RatesRepository

    companion object {
        @Provides
        fun provideRatesStringConverter(): ClassToStringConverter<List<RateModel>> {
            return MoshiRatesConverter()
        }
    }
}

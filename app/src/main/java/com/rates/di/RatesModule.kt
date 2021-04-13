package com.rates.di

import com.rates.model.GetRatesUseCase
import com.rates.model.GetRatesUseCaseImpl
import com.rates.model.RatesCalculator
import com.rates.model.RatesCalculatorImpl
import com.rates.ui.adapter.RateToRateUiModelAdapter
import com.rates.ui.adapter.RateToRateUiModelAdapterImpl
import com.rates.ui.adapter.RatesToRateUiModelsAdapter
import com.rates.ui.adapter.RatesToRateUiModelsAdapterImpl
import dagger.Binds
import dagger.Module
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
    fun provideRateToRateUiModelAdapter(rateToRateUiModelAdapterImpl: RateToRateUiModelAdapterImpl): RateToRateUiModelAdapter
}
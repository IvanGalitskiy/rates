package com.rates.ui

import com.rates.data.NetworkRatesRepository
import com.rates.data.RatesRepository
import com.rates.di.RatesModule
import com.rates.model.*
import com.rates.ui.adapter.RatesToRateUiModelsAdapter
import com.rates.ui.adapter.RatesToRateUiModelsAdapterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [RatesModule::class]
)
abstract class TestRatesModule {
    @Binds
    abstract fun provideGetRatesUseCase(getRatesUseCaseImpl: GetRatesUseCaseImpl): GetRatesUseCase


    @Binds
    abstract fun provideRatesCalculator(ratesCalculator: RatesCalculatorImpl): RatesCalculator


    @Binds
    abstract fun provideRatesRepository(ratesRepository: NetworkRatesRepository): RatesRepository

    companion object {
        @Provides
        fun provideRatesToRateUiModelsAdapter(): RatesToRateUiModelsAdapter {
            return object : RatesToRateUiModelsAdapter {
                private val adapter = RatesToRateUiModelsAdapterImpl()
                override fun map(rates: List<RateModel>): List<RateUiModel> {
                    return adapter.map(rates)
                        .sortedBy { it.rateName }
                }
            }
        }
    }
}
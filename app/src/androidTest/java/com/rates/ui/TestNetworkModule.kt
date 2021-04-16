package com.rates.ui

import com.rates.data.RatesApi
import com.rates.di.NetworkModule
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [NetworkModule::class]
)
interface TestNetworkModule {
    @Binds
    fun provideApi(api: ApiStub): RatesApi
}
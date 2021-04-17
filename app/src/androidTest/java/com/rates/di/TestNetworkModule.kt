package com.rates.di

import com.rates.ApiStub
import com.rates.data.RatesApi
import com.rates.di.ApplicationModule
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [ApplicationModule::class]
)
interface TestNetworkModule {
    @Binds
    fun provideApi(api: ApiStub): RatesApi
}
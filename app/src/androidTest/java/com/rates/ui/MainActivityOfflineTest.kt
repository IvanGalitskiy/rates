package com.rates.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.rates.R
import com.rates.data.ClassToStringConverter
import com.rates.data.MoshiRatesConverter
import com.rates.data.RatesRepository
import com.rates.di.RatesModule
import com.rates.model.*
import com.rates.ui.adapter.RatesToRateUiModelsAdapter
import com.rates.ui.adapter.RatesToRateUiModelsAdapterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.kotlin.mock
import java.net.UnknownHostException

@UninstallModules(RatesModule::class)
@HiltAndroidTest
class MainActivityOfflineTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    val repository: RatesRepository = mock()

    @Test
    fun shouldShowSnackbarIfNoInternetConnection() {
        `when`(repository.getRates(anyString())).thenReturn(
            Single.just(
                GetRatesResponse(
                    "EUR",
                    listOf(RateModel("AUD", 50.0)),
                    UnknownHostException()
                )
            )
        )
        launchActivity<MainActivity>()
        Espresso.onView(
            withText(
                ApplicationProvider.getApplicationContext<Context>()
                    .getString(R.string.network_error)
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowSnackbarIfNoDataWasFoundEvenIfExceptionIsNotExpected() {
        `when`(repository.getRates(anyString())).thenReturn(
            Single.just(
                GetRatesResponse("EUR", emptyList(), UnknownHostException())
            )
        )
        launchActivity<MainActivity>()
        Espresso.onView(
            withText(
                ApplicationProvider.getApplicationContext<Context>()
                    .getString(R.string.missing_data_error)
            )
        ).check(matches(isDisplayed()))
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    abstract class TestRatesModule {
        @Binds
        abstract fun provideGetRatesUseCase(getRatesUseCaseImpl: GetRatesUseCaseImpl): GetRatesUseCase


        @Binds
        abstract fun provideRatesCalculator(ratesCalculator: RatesCalculatorImpl): RatesCalculator

        companion object {
            @Provides
            fun converter(): ClassToStringConverter<List<RateModel>> {
                return MoshiRatesConverter()
            }

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
}
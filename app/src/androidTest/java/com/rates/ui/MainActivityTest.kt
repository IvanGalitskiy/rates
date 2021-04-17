package com.rates.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.rates.ApiStub.Companion.FIRST_TESTING_VARIANT
import com.rates.ApiStub.Companion.SECOND_TESTING_VARIANT
import com.rates.R
import com.rates.data.ClassToStringConverter
import com.rates.data.DefaultRatesRepository
import com.rates.data.MoshiRatesConverter
import com.rates.data.RatesRepository
import com.rates.di.RatesModule
import com.rates.model.*
import com.rates.ui.adapter.RatesToRateUiModelsAdapter
import com.rates.ui.adapter.RatesToRateUiModelsAdapterImpl
import com.rates.ui.list.RatesHolder
import com.rates.utils.DisableAnimationsRule
import com.rates.utils.atPosition
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

@UninstallModules(RatesModule::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var disableAnimationsRule = DisableAnimationsRule()

    @Test
    fun shouldContainDefaultRequestedValuesAtFirstPositionWhenStarted() {
        launchActivity<MainActivity>()
        onView(withId(R.id.activity_main_list)).check(
            matches(
                atPosition(
                    0,
                    hasDescendant(withText(RatesRequest.DEFAULT_RATE_NAME))
                )
            )
        )
        onView(withId(R.id.activity_main_list)).check(
            matches(
                atPosition(
                    0,
                    hasDescendant(withText("%.2f".format(RatesRequest.DEFAULT_RATE_AMOUNT)))
                )
            )
        )
    }

    @Test
    fun shouldNotChangeAmountOfFirstItem() {
        launchActivity<MainActivity>()
        Thread.sleep(GetRatesUseCaseImpl.REQUEST_INTERVAL_IN_MILLIS)
        onView(withId(R.id.activity_main_list)).check(
            matches(
                atPosition(
                    0,
                    hasDescendant(withText("%.2f".format(RatesRequest.DEFAULT_RATE_AMOUNT)))
                )
            )
        )
    }

    @Test
    fun shouldChangeAmountOfAllItemsExceptFirst() {
        launchActivity<MainActivity>()
        onView(withId(R.id.activity_main_list)).check(
            matches(
                atPosition(
                    1,
                    hasDescendant(
                        withText(
                            getFinalDisplayValue(
                                SECOND_TESTING_VARIANT[0].second,
                                RatesRequest.DEFAULT_RATE_AMOUNT
                            )
                        )
                    )
                )
            )
        )
        Thread.sleep(GetRatesUseCaseImpl.REQUEST_INTERVAL_IN_MILLIS)
        onView(withId(R.id.activity_main_list)).check(
            matches(
                atPosition(
                    1,
                    hasDescendant(
                        withText(
                            getFinalDisplayValue(
                                FIRST_TESTING_VARIANT[0].second,
                                RatesRequest.DEFAULT_RATE_AMOUNT
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun shouldChangeAllItemsWhenFirstItemChangedAmount() {
        launchActivity<MainActivity>()
        onView(
            allOf(
                withId(R.id.item_rate_input),
                withText(formatAmount(RatesRequest.DEFAULT_RATE_AMOUNT))
            )
        ).perform(replaceText("10.00"))
        onView(withId(R.id.activity_main_list)).check(
            matches(
                atPosition(
                    1,
                    hasDescendant(
                        withText(
                            getFinalDisplayValue(
                                SECOND_TESTING_VARIANT[0].second,
                                10.0
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun shouldMoveItemToFirstPositionIfItWasClicked() {
        launchActivity<MainActivity>()
        onView(withId(R.id.activity_main_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RatesHolder>(1, click()))
        onView(withId(R.id.activity_main_list)).check(
            matches(
                atPosition(
                    0,
                    hasDescendant(withText(FIRST_TESTING_VARIANT[0].first))
                )
            )
        )
    }

    @Test
    fun shouldDisplayErrorIfNoConnection() {
        launchActivity<MainActivity>()
    }

    private fun getFinalDisplayValue(testingVariantAmount: Double, amount: Double): String {
        return formatAmount(testingVariantAmount * amount)
    }

    private fun formatAmount(amount: Double): String {
        return "%.2f".format(amount)
    }


    @Module
    @InstallIn(ViewModelComponent::class)
    abstract class TestRatesModule {
        @Binds
        abstract fun provideGetRatesUseCase(getRatesUseCaseImpl: GetRatesUseCaseImpl): GetRatesUseCase


        @Binds
        abstract fun provideRatesCalculator(ratesCalculator: RatesCalculatorImpl): RatesCalculator


        @Binds
        abstract fun provideRatesRepository(ratesRepository: DefaultRatesRepository): RatesRepository

        companion object {
            @Provides
            fun converter(): ClassToStringConverter<List<RateModel>>{
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
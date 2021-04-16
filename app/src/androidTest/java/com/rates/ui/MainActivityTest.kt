package com.rates.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.rates.R
import com.rates.model.GetRatesUseCaseImpl
import com.rates.model.RatesRequest
import com.rates.ui.ApiStub.Companion.FIRST_TESTING_VARIANT
import com.rates.ui.ApiStub.Companion.SECOND_TESTING_VARIANT
import com.rates.ui.list.RatesHolder
import com.rates.utils.DisableAnimationsRule
import com.rates.utils.atPosition
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

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

    private fun getFinalDisplayValue(testingVariantAmount: Double, amount: Double): String {
        return formatAmount(testingVariantAmount * amount)
    }

    private fun formatAmount(amount: Double): String{
        return "%.2f".format(amount)
    }
}
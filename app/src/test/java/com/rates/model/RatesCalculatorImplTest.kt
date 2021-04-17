package com.rates.model

import org.junit.Test

class RatesCalculatorImplTest {

    @Test
    fun `should calculate rate properly`() {
        // arrange
        val calculator = RatesCalculatorImpl()
        val activeAmount = 10.0
        val rates = listOf(RateModel("RATE_1", 1.0), RateModel("RATE_2", 2.0))
        // act
        val calculationResult = calculator.recalculateRatesForAmount(rates, activeAmount)
        // assert
        calculationResult.test()
            .assertValue {
                it.find { rate -> rate.rateName == rates[0].rateName }!!.amount == 10.0 &&
                        it.find { rate -> rate.rateName == rates[1].rateName }!!.amount == 20.0
            }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should skip invalid data for calculation`() {
        // arrange
        val calculator = RatesCalculatorImpl()
        val activeAmount = 10.0
        val rates = listOf(RateModel("RATE_1", -1.0), RateModel("RATE_2", 2.0))
        // act
        val calculationResult = calculator.recalculateRatesForAmount(rates, activeAmount)
        // assert
        calculationResult.test()
            .assertValue {
                it.find { rate -> rate.rateName == rates[0].rateName } == null &&
                        it.find { rate -> rate.rateName == rates[1].rateName }!!.amount == 20.0
            }
            .assertNoErrors()
            .dispose()
    }
}

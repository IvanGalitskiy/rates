package com.rates.model

import org.junit.Test

class RatesCalculatorImplTest {

    @Test
    fun `should calculate rate properly`() {
        // arrange
        val calculator = RatesCalculatorImpl()
        val activeAmount = 10.0
        val rates = mapOf("RATE_1" to 1.0, "RATE_2" to 2.0)
        // act
        val calculationResult = calculator.recalculateRatesForAmount(rates, activeAmount)
        // assert
        calculationResult.test()
            .assertValue {
                it["RATE_1"] == 10.0 &&
                        it["RATE_2"] == 20.0
            }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should skip invalid data for calculation`() {
        // arrange
        val calculator = RatesCalculatorImpl()
        val activeAmount = 10.0
        val rates = mapOf("RATE_1" to -1.0, "RATE_2" to 2.0)
        // act
        val calculationResult = calculator.recalculateRatesForAmount(rates, activeAmount)
        // assert
        calculationResult.test()
            .assertValue {
                !it.contains("RATE_1") &&
                        it["RATE_2"] == 20.0
            }
            .assertNoErrors()
            .dispose()
    }
}

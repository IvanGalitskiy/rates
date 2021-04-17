package com.rates.model

import com.rates.data.RatesRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.AdditionalMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import java.util.concurrent.TimeUnit


class GetRatesUseCaseImplTest {

    @Mock
    lateinit var repository: RatesRepository

    @Spy
    lateinit var calculator: RatesCalculatorImpl


    private val baseRates = GetRatesResponse("EUR", listOf(RateModel("RATE_1", 1.0), RateModel("RATE_2", 2.0)))

    lateinit var getRatesUseCase: GetRatesUseCaseImpl
    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(repository.getRates(anyString())).thenReturn(
            Single.just(baseRates)
        )
        getRatesUseCase =
            GetRatesUseCaseImpl(repository, calculator, TESTING_REPEAT_INTERVAL_IN_MILLIS)
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @Test
    fun `should start observing with default params if specific are not present`() {
        // act
        val observer = getRatesUseCase.observeRates(RatesRequest())
            .test()
            .assertNoErrors()

        testScheduler.advanceTimeBy(TESTING_REPEAT_INTERVAL_IN_MILLIS / 2, TimeUnit.MILLISECONDS)

        // assert
        verify(repository).getRates(RatesRequest.DEFAULT_RATE_NAME)
        verify(calculator).recalculateRatesForAmount(baseRates.rates, RatesRequest.DEFAULT_RATE_AMOUNT)

        observer.dispose()
    }

    @Test
    fun `should emit new values each time period`() {
        // act
        val observer = getRatesUseCase.observeRates(RatesRequest())
            .test()
            .assertNoErrors()
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        // assert
        observer.assertValueCount(11)

        observer.dispose()
    }

    @Test
    fun `should skip repo if only amount was changed`() {
        // arrange
        val ratesObservable = getRatesUseCase.observeRates(RatesRequest())
        val observer = ratesObservable
            .takeUntil(Observable.timer(150, TimeUnit.SECONDS))
            .test()

        testScheduler.advanceTimeBy(TESTING_REPEAT_INTERVAL_IN_MILLIS / 2, TimeUnit.MILLISECONDS)
        // act
        getRatesUseCase.onRateAmountChanged(150.0)
        // assert
        verify(repository, times(1)).getRates(RatesRequest.DEFAULT_RATE_NAME)
        verify(calculator, times(1)).recalculateRatesForAmount(
            any(),
            eq(RatesRequest.DEFAULT_RATE_AMOUNT)
        )
        verify(calculator, times(1)).recalculateRatesForAmount(any(), eq(150.0))

        observer.dispose()
    }

    @Test
    fun `should call only recalculation if same rate name was given`() {
        // arrange
        val ratesObservable = getRatesUseCase.observeRates(RatesRequest())
        val observer = ratesObservable
            .takeUntil(Observable.timer(150, TimeUnit.SECONDS))
            .test()
            .assertNoErrors()
        testScheduler.advanceTimeBy(TESTING_REPEAT_INTERVAL_IN_MILLIS / 2, TimeUnit.MILLISECONDS)
        // act
        getRatesUseCase.onBaseRateChanged(RatesRequest.DEFAULT_RATE_NAME, 150.0)
        // assert
        verify(repository, times(1)).getRates(eq(RatesRequest.DEFAULT_RATE_NAME))
        verify(calculator, times(2)).recalculateRatesForAmount(any(), any())

        observer.dispose()
    }

    @Test
    fun `should change request values if new one was given`() {
        // arrange
        val ratesObservable = getRatesUseCase.observeRates(RatesRequest())
        val observer = ratesObservable
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testScheduler.advanceTimeBy(TESTING_REPEAT_INTERVAL_IN_MILLIS / 2, TimeUnit.MILLISECONDS)
        // act
        getRatesUseCase.onBaseRateChanged("STR", 150.0)
        // assert
        verify(repository, times(1)).getRates(eq(RatesRequest.DEFAULT_RATE_NAME))
        verify(repository, times(1)).getRates(eq("STR"))
        verify(calculator, times(1)).recalculateRatesForAmount(any(), eq(100.0))
        verify(calculator, times(1)).recalculateRatesForAmount(any(), eq(150.0))

        observer.dispose()
    }

    @Test
    fun `should use default time period if not provided`() {
        // arrange
        getRatesUseCase = GetRatesUseCaseImpl(repository, calculator)
        val ratesObservable = getRatesUseCase.observeRates(RatesRequest())
        // act
        val observer = ratesObservable
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testScheduler.advanceTimeBy(9, TimeUnit.SECONDS)
        // assert
        // first value is emitted without delay, so we will have seconds + 1 here
        observer.assertValueCount(10)

        observer.dispose()
    }

    companion object {
        private const val TESTING_REPEAT_INTERVAL_IN_MILLIS = 100L
    }
}

package com.rates.data

import com.rates.model.GetRatesResponse
import com.rates.model.RateModel
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.net.UnknownHostException

class DefaultRatesRepositoryTest {

    @Mock
    lateinit var networkRatesRepository: NetworkRatesRepository

    @Mock
    lateinit var localRatesRepository: LocalRatesRepository

    lateinit var defaultRatesRepository: DefaultRatesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        defaultRatesRepository =
            DefaultRatesRepository(networkRatesRepository, localRatesRepository)
        `when`(networkRatesRepository.getRates("EUR")).thenReturn(
            Single.just(NETWORK_RESULT)
        )
        `when`(localRatesRepository.getRates("EUR")).thenReturn(
            Single.just(LOCAL_RESULT)
        )
    }

    @Test
    fun `should return fresh data if present`() {
        // act
        val observer = defaultRatesRepository.getRates("EUR").test()
        // assert
        observer.assertValue { it == NETWORK_RESULT }

        observer.dispose()
    }

    @Test
    fun `should store fresh data in local repo`() {
        // act
        val observer = defaultRatesRepository.getRates("EUR").test()
        // assert
        verify(localRatesRepository).saveLastKnownRate(NETWORK_RESULT)

        observer.dispose()
    }

    @Test
    fun `should return local data if network throws error`() {
        // Arrange
        `when`(networkRatesRepository.getRates("EUR")).thenReturn(
            Single.error(NETWORK_ERROR)
        )
        // act
        val observer = defaultRatesRepository.getRates("EUR").test()
        // assert
        verify(localRatesRepository, never()).saveLastKnownRate(any())
        observer.assertValue {
            it == LOCAL_RESULT
        }

        observer.dispose()
    }

    companion object {
        private val NETWORK_ERROR = UnknownHostException()
        private val NETWORK_RESULT = GetRatesResponse(
            "EUR", listOf(
                RateModel("AUD", 50.0)
            )
        )
        private val LOCAL_RESULT = GetRatesResponse(
            "EUR", listOf(
                RateModel("USD", 10.0),
            ),
            NETWORK_ERROR
        )
    }
}
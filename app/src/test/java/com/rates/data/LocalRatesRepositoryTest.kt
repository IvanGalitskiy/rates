package com.rates.data

import android.content.Context
import android.content.SharedPreferences
import com.rates.model.GetRatesResponse
import com.rates.model.RateModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class LocalRatesRepositoryTest {
    @Mock
    lateinit var preferences: SharedPreferences

    @Mock
    lateinit var editor: SharedPreferences.Editor

    @Mock
    lateinit var contextMock: Context

    lateinit var localRatesRepository: LocalRatesRepository

    private val moshiConverter = MoshiRatesConverter()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(contextMock.getSharedPreferences(any(), any())).thenReturn(preferences)
        `when`(preferences.edit()).thenReturn(editor)
        `when`(editor.putString(any(), any())).thenReturn(editor)
        localRatesRepository = LocalRatesRepository(contextMock, MoshiRatesConverter())
    }

    @Test
    fun `should store converted data in preference`() {
        // arrange
        val response = GetRatesResponse(
            "EUR",
            listOf(RateModel("AUD", 50.0))
        )
        // act
        localRatesRepository.saveLastKnownRate(response)
        // assert
        verify(editor).putString("EUR", moshiConverter.convertToString(response.rates))
    }

    @Test
    fun `should get data from preference if requested`() {
        // act
        val observer = localRatesRepository.getRates("EUR").test()
        // assert
        verify(preferences).getString("EUR", null)

        observer.dispose()
    }

    @Test
    fun `should return default value if there is no records`() {
        // arrange
        `when`(preferences.getString(any(), any())).thenReturn(null)
        // act
        val observer = localRatesRepository.getRates("EUR").test()
        // assert
        observer.assertValue {
            it == GetRatesResponse("EUR", emptyList())
        }

        observer.dispose()
    }

    @Test
    fun `should return value from preferences if present`() {
        // arrange
        val mockedConverter = mock<MoshiRatesConverter>()
        localRatesRepository = LocalRatesRepository(contextMock, mockedConverter)
        val expectedRates = listOf(
            RateModel(
                "AUD",
                50.0
            )
        )
        `when`(mockedConverter.convertToClass(anyString())).thenReturn(expectedRates)
        `when`(preferences.getString(anyString(), anyOrNull())).thenReturn("qwerty")
        // act
        val observer = localRatesRepository.getRates("EUR").test()
        // assert
        observer.assertValue {
            it == GetRatesResponse("EUR", expectedRates)
        }

        observer.dispose()
    }
}

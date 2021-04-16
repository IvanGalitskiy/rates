package com.rates.data

import io.reactivex.rxjava3.core.Single
import org.junit.Test
import java.net.UnknownHostException

class NetworkRatesRepositoryTest {
    @Test
    fun `should return expected result from response`(){
        // arrange
        val expectedResponse = RatesResponse("EUR", mapOf(Pair("EUR", 100.00)))
        val apiStub = object : RatesApi{
            override fun getRates(base: String): Single<RatesResponse> {
                return Single.just(expectedResponse)
            }
        }
        val repository = NetworkRatesRepository(apiStub)
        // act
        val repositoryResult = repository.getRates("EUR")
        // assert
        repositoryResult.test()
            .assertResult(expectedResponse.rates)
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should return error if api throws it`(){
        // arrange
        val apiStub = object : RatesApi{
            override fun getRates(base: String): Single<RatesResponse> {
                return Single.error(UnknownHostException())
            }
        }
        val repository = NetworkRatesRepository(apiStub)
        // act
        val repositoryResult = repository.getRates("EUR")
        // assert
        repositoryResult.test()
            .assertError { it is UnknownHostException }
            .dispose()
    }
}
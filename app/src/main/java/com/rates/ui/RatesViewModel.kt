package com.rates.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rates.errors.NoLocalDataFoundException
import com.rates.model.GetRatesUseCase
import com.rates.model.RatesRequest
import com.rates.ui.adapter.RatesToRateUiModelsAdapter
import com.rates.utils.disposeIfNeeded
import com.rates.utils.isNetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val ratesToRateUiModelAdapter: RatesToRateUiModelsAdapter
) : ViewModel() {

    private var ratesDisposable: Disposable? = null
    private val ratesLiveData = MutableLiveData<List<RateUiModel>>()
    private val errorsLiveData = MutableLiveData<Throwable?>()

    fun observeRates(): LiveData<List<RateUiModel>> = ratesLiveData

    fun observeErrors(): LiveData<Throwable?> = errorsLiveData

    /**
     * screen is hidden, we do not need to eat internet traffic
     */
    fun onScreenHidden() {
        ratesDisposable.disposeIfNeeded()
    }

    /**
     * screen is appeared, we have to start rates listening
     */
    fun onScreenAppeared() {
        startRatesObserving()
    }

    fun onBaseRateChanged(rateName: String, amount: Double) {
        getRatesUseCase.onBaseRateChanged(rateName, amount)
    }

    fun onConnectionEstablished() {
        ratesDisposable.disposeIfNeeded()
        errorsLiveData.postValue(null)
        startRatesObserving(null)
    }

    private fun startRatesObserving() {
        ratesDisposable.disposeIfNeeded()
        startRatesObserving(RatesRequest())
    }

    private fun startRatesObserving(ratesRequest: RatesRequest?) {
        ratesDisposable.disposeIfNeeded()
        ratesDisposable = getRatesUseCase.observeRates(ratesRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (it.rates.size == 1) {
                    postError(NoLocalDataFoundException())
                } else {
                    postError(it.exception)
                }
            }
            .map { ratesToRateUiModelAdapter.map(it.rates) }
            .subscribe({

                ratesLiveData.postValue(it)
            }, {
                postError(it)
            })
    }

    private fun postError(newError: Throwable?) {
        val oldError = errorsLiveData.value
        if (oldError == null ||
            newError == null ||
            (oldError.isNetworkError() && !newError.isNetworkError() ||
            !oldError.isNetworkError() && newError.isNetworkError()) &&
                    oldError::class != newError::class
        ) {
            errorsLiveData.postValue(newError)
        }
    }
}
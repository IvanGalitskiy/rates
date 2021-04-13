package com.rates.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rates.model.GetRatesUseCase
import com.rates.ui.adapter.RatesToRateUiModelsAdapter
import com.rates.utils.disposeIfNeeded
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
    private val ratesLiveData = MutableLiveData<RatesState>()

    // area for improvements
    // persistent storage of last used values
    // it will create better UX bcs user will not be needed to seek needed currency each time
    var lastUsedRate: String = DEFAULT_RATE_NAME
    var lastUsedAmount: Double = DEFAULT_RATE_AMOUNT

    fun observeRates(): LiveData<RatesState> = ratesLiveData

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
        startRatesObserving(lastUsedRate, lastUsedAmount)
    }

    override fun onCleared() {
        ratesDisposable.disposeIfNeeded()
        super.onCleared()
    }

    fun onBaseRateChanged(rateName: String, amount: Double) {
        lastUsedRate = rateName
        lastUsedAmount = amount
        getRatesUseCase.onBaseRateChanged(rateName, amount)
    }

    fun onRateAmountChanged(amount: Double) {
        lastUsedAmount = amount
        getRatesUseCase.onRateAmountChanged(amount)
    }

    private fun startRatesObserving(rateName: String, amount: Double) {
        ratesDisposable.disposeIfNeeded()
        ratesDisposable = getRatesUseCase.observeRates(rateName, amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { ratesToRateUiModelAdapter.map(it) }
            .subscribe({
                ratesLiveData.postValue(RatesState.Success(it))
            }, {
                ratesLiveData.postValue(RatesState.Error(it))
            })
    }

    companion object {
        private const val DEFAULT_RATE_NAME = "EUR"
        private const val DEFAULT_RATE_AMOUNT = 0.0
    }
}
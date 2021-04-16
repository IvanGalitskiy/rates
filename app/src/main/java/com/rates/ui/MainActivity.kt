package com.rates.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.rates.R
import com.rates.databinding.ActivityMainBinding
import com.rates.ui.list.OnRateChangedCallback
import com.rates.ui.list.RatesAdapter
import com.rates.utils.isNetworkError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val ratesViewModel: RatesViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val adapter = RatesAdapter(object : OnRateChangedCallback {
        override fun onRateChanged(rateName: String, amount: Double) {
            ratesViewModel.onBaseRateChanged(rateName, amount)
        }

        override fun onUnexpectedTextEntered() {
            // todo
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        binding.activityMainList.adapter = adapter
        observeViewModels()
    }

    private fun setUpActionBar() {
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    private fun observeViewModels() {
        ratesViewModel.observeRates()
            .observe(this, {
                when (it) {
                    is RatesState.Success -> {
                        adapter.submitList(it.rates.toMutableList())
                        adapter.submitList(it.rates)
                    }
                    is RatesState.Error -> {
                        if (it.exception.isNetworkError()) {
                            ErrorMessage.showNetworkError(binding.root) {
                                ratesViewModel.onConnectionEstablished()
                            }
                        }
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        ratesViewModel.onScreenAppeared()
    }

    override fun onStop() {
        super.onStop()
        ratesViewModel.onScreenHidden()
    }
}
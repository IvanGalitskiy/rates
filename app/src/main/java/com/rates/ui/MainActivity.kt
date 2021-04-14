package com.rates.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.rates.R
import com.rates.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val ratesViewModel: RatesViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val adapter = RatesAdapter()

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
                        adapter.rates = it.rates.toMutableList()
                    }
                    is RatesState.Error -> {
                        // todo
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
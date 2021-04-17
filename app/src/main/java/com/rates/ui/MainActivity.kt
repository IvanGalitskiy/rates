package com.rates.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.rates.R
import com.rates.databinding.ActivityMainBinding
import com.rates.errors.NoLocalDataFoundException
import com.rates.ui.list.FirstLastItemOffsetDecorator
import com.rates.ui.list.OnRateChangedCallback
import com.rates.ui.list.RatesAdapter
import com.rates.utils.isNetworkError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val ratesViewModel: RatesViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val edgeItemDecorator by lazy {
        FirstLastItemOffsetDecorator(
            this.resources.getDimensionPixelSize(
                R.dimen.top_list_offset
            ), this.resources.getDimensionPixelSize(R.dimen.bottom_list_offset)
        )
    }

    private val adapter = RatesAdapter(object : OnRateChangedCallback {
        override fun onRateChanged(rateName: String, amount: Double) {
            ratesViewModel.onBaseRateChanged(rateName, amount)
        }

        override fun onUnexpectedTextEntered() {
            ErrorMessage.showUnexpectedTypingError(binding.root)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        binding.activityMainList.adapter = adapter
        (binding.activityMainList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
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
                adapter.submitList(it)
            })

        ratesViewModel.observeErrors()
            .observe(this, {
                if (it == null) {
                    if (binding.activityMainList.itemDecorationCount > 0) {
                        binding.activityMainList.removeItemDecoration(edgeItemDecorator)
                    }
                    ErrorMessage.dismissAllErrors()
                } else {
                    if (binding.activityMainList.itemDecorationCount == 0) {
                        binding.activityMainList.addItemDecoration(edgeItemDecorator)
                    }
                    if (it.isNetworkError()) {
                        ErrorMessage.showNetworkError(binding.root) {
                            ratesViewModel.onConnectionEstablished()
                        }
                    } else if (it is NoLocalDataFoundException) {
                        ErrorMessage.showNoDataError(binding.root) {
                            ratesViewModel.onConnectionEstablished()
                        }
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
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
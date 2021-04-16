package com.rates.ui.list

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.rates.R
import com.rates.ui.RateUiModel

class RatesHolder(
    binding: View,
    private val onRateChangedCallback: OnRateChangedCallback,
    private val onActiveRateChanged: (RateUiModel) -> Unit
) :
    RecyclerView.ViewHolder(binding) {
    private var rateInput: EditText
    private var rateName: TextView
    private var currencyName: TextView
    private var rateIcon: ImageView
    private var textWatcher: TextWatcher? = null

    init {
        binding.apply {
            rateInput = findViewById(R.id.item_rate_input)
            rateName = findViewById(R.id.item_rate_title)
            currencyName = findViewById(R.id.item_rate_subhead)
            rateIcon = findViewById(R.id.item_rate_icon)
        }
    }

    fun bind(data: RateUiModel, isActive: Boolean) {
        rateName.text = data.rateName
        currencyName.text = data.currencyName
        data.icon?.let {
            rateIcon.setImageResource(data.icon)
        }

        if (isActive) {
            bindActive(data)
        } else {
            bindInactive(data)
        }
    }

    private fun bindActive(data: RateUiModel) {
        rateInput.apply {
            onFocusChangeListener = null
            requestFocus()
            setText(data.amount)
            itemView.setOnClickListener(null)
            textWatcher = doAfterTextChanged {
                notifyRateChanged(data, it)
            }
            notifyRateChanged(data, text)
        }
    }

    private fun bindInactive(data: RateUiModel) {
        val focusListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onActiveRateChanged(data)
            }
        }
        itemView.setOnClickListener { onActiveRateChanged(data) }
        rateInput.apply {
            textWatcher?.let { removeTextChangedListener(it) }
            setText(data.amount)
            onFocusChangeListener = focusListener
        }
    }

    private fun notifyRateChanged(rate: RateUiModel, editTextContent: Editable?) {
        val amount = parseInputToDouble(editTextContent)
        amount?.let {
            onRateChangedCallback.onRateChanged(rate.rateName, amount)
        } ?: onRateChangedCallback.onUnexpectedTextEntered()
    }

    private fun parseInputToDouble(editable: Editable?): Double? {
        return editable?.toString()?.toDoubleOrNull()
    }
}

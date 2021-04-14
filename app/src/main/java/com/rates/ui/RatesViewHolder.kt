package com.rates.ui

import androidx.recyclerview.widget.RecyclerView
import com.rates.databinding.ItemRateBinding

class RatesViewHolder(private val binding: ItemRateBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(rate: RateUiModel) {
        binding.itemRateTitle.text = rate.rateName
        binding.itemRateSubhead.text = rate.currencyName
        binding.imageView.setImageDrawable(rate.icon)
        // todo edit text ?
    }
}
package com.rates.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rates.databinding.ItemRateBinding

class RatesAdapter : RecyclerView.Adapter<RatesViewHolder>() {
    var rates = mutableListOf<RateUiModel>()
        set(value) {
            field.clear()
            field.addAll(value)
            // todo DiffUtils
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val itemBinding =
            ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(rates[position])
    }

    override fun getItemCount(): Int = rates.size
}
package com.rates.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rates.R
import com.rates.ui.RateUiModel

class RatesAdapter(private val onRateChangedCallback: OnRateChangedCallback) :
    RecyclerView.Adapter<RatesHolder>() {
    private var items = mutableListOf<RateUiModel>()
    lateinit var recycler: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recycler = recyclerView
    }

    fun submitList(newItems: List<RateUiModel>) {
        newItems.forEach { newModel ->
            val existingRate = items.find { it.rateName == newModel.rateName }
            if (existingRate != null) {
                existingRate.amount = newModel.amount
            } else {
                items.add(newModel)
            }
        }
        notifyItemRangeChanged(1, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesHolder {
        return RatesHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false),
            onRateChangedCallback
        ) {
            val index = items.indexOf(it)
            items.removeAt(index)
            items.add(0, it)
            notifyItemMoved(index, 0)
            notifyItemRangeChanged(0, index)
            recycler.scrollToPosition(0)
        }
    }

    override fun onBindViewHolder(holder: RatesHolder, position: Int) {
        holder.bind(items[position], position == 0)
    }

    override fun getItemCount(): Int = items.size
}



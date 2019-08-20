package com.lcabrales.simgas.ui.dialog.adapter

import androidx.recyclerview.widget.RecyclerView
import com.lcabrales.simgas.databinding.ItemAirQualityBinding

class AirQualityItemViewHolder(private val binding: ItemAirQualityBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): ItemAirQualityBinding {
        return binding
    }
}
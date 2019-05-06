package com.lcabrales.simgas.ui.sensorreadings.adapter

import androidx.recyclerview.widget.RecyclerView
import com.lcabrales.simgas.databinding.ItemSensorReadingBinding

class SensorReadingItemViewHolder(private val binding: ItemSensorReadingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): ItemSensorReadingBinding {
        return binding
    }
}
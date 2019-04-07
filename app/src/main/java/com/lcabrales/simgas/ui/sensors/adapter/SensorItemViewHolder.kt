package com.lcabrales.simgas.ui.sensors.adapter

import androidx.recyclerview.widget.RecyclerView
import com.lcabrales.simgas.databinding.ItemSensorBinding

class SensorItemViewHolder(private val binding: ItemSensorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): ItemSensorBinding {
        return binding
    }
}
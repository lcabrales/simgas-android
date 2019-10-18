package com.lcabrales.simgas.ui.dialog.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.ItemAirQualityBinding
import com.lcabrales.simgas.model.airquality.AirQuality

class AirQualityAdapter(private val dataset: List<AirQuality>) :
    RecyclerView.Adapter<AirQualityItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirQualityItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemAirQualityBinding.inflate(layoutInflater, parent, false)
        return AirQualityItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: AirQualityItemViewHolder, position: Int) {
        val item = getItem(position)

        val context = holder.getBinding().tvMinValue.context

        holder.getBinding().tvMinValue.text = item.minValue.toString()
        holder.getBinding().tvMaxValue.text = if (item.maxValue == null) context.getString(
            R.string.infinity) else item.maxValue.toString()
        holder.getBinding().tvAirQualityValue.text = item.name

        val airQualityColor = Color.parseColor(item.primaryColor)
        holder.getBinding().tvAirQualityValue.setTextColor(airQualityColor)
    }

    private fun getItem(position: Int): AirQuality {
        return dataset[position]
    }
}
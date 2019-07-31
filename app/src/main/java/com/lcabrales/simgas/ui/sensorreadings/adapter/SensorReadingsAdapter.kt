package com.lcabrales.simgas.ui.sensorreadings.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lcabrales.simgas.R
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.databinding.ItemSensorReadingBinding
import com.lcabrales.simgas.model.readings.SensorReading

class SensorReadingsAdapter(private val dataset: List<SensorReading>) :
    RecyclerView.Adapter<SensorReadingItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorReadingItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemSensorReadingBinding.inflate(layoutInflater, parent, false)
        return SensorReadingItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: SensorReadingItemViewHolder, position: Int) {
        val item = getItem(position)

        val context = holder.getBinding().tvValue.context

        holder.getBinding().tvValue.text = context.getString(R.string.ppm_value,
            item.gasPpm)

        val date = Dates.getFormattedDate(item.createdDate, Dates.SERVER_FORMAT)
        holder.getBinding().tvDate.text = Dates.getRelativeTimeString(date.time)

        val progressVal = (item.gasPercentage!! * holder.getBinding().pbValue.max).toInt()
        holder.getBinding().pbValue.progress = progressVal

        val airQualityColor = Color.parseColor(item.airQuality?.primaryColor)
        holder.getBinding().pbValue.progressTintList = ColorStateList.valueOf(airQualityColor)
    }

    private fun getItem(position: Int): SensorReading {
        return dataset[position]
    }
}
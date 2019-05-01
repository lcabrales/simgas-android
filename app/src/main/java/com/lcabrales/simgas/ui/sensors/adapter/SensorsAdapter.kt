package com.lcabrales.simgas.ui.sensors.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.databinding.ItemSensorBinding
import com.lcabrales.simgas.model.sensors.Sensor

class SensorsAdapter(private val dataset: List<Sensor>) :
    RecyclerView.Adapter<SensorItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemSensorBinding.inflate(layoutInflater, parent, false)
        return SensorItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: SensorItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.getBinding().tvTitle.text = item.name
        holder.getBinding().tvSubtitle.text = item.gas?.name
        holder.getBinding().tvLastReading.text = item.lastSensorReading?.gasPpm.toString()

        val date = Dates.getFormattedDate(item.lastSensorReading?.createdDate, Dates.SERVER_FORMAT)
        holder.getBinding().tvLastReadingDate.text = Dates.getRelativeTimeString(date.time)

        val progressVal = (item.lastSensorReading?.gasPercentage!! * holder.getBinding().pbLastReading.max).toInt()
        holder.getBinding().pbLastReading.progress = progressVal

        val airQualityColor = Color.parseColor(item.lastSensorReading?.airQuality?.primaryColor)
        holder.getBinding().pbLastReading.progressTintList = ColorStateList.valueOf(airQualityColor)
    }

    private fun getItem(position: Int): Sensor {
        return dataset[position]
    }
}
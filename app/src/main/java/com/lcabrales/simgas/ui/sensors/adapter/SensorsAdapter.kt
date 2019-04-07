package com.lcabrales.simgas.ui.sensors.adapter

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
        holder.getBinding().tvLastReadingDate.text = Dates.getFormattedDateString(
            item.lastSensorReading?.createdDate, Dates.SERVER_FORMAT, Dates.DISPLAY_FORMAT_TIME)
        holder.getBinding().pbLastReading.progress = item.lastSensorReading?.gasPpm!!

        //todo colored progress bar
    }

    private fun getItem(position: Int): Sensor {
        return dataset[position]
    }
}
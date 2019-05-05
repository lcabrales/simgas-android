package com.lcabrales.simgas.model.readings.daily

import com.lcabrales.simgas.model.sensors.Sensor
import com.squareup.moshi.Json

data class DailyAverageReading(
    @Json(name = "Sensor")
    var sensor: Sensor? = null,
    @Json(name = "DailyAverages")
    var dailyAverages: List<DailyAverage>? = null
)
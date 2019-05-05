package com.lcabrales.simgas.model.readings.daily

import com.lcabrales.simgas.model.airquality.AirQuality
import com.squareup.moshi.Json

data class DailyAverage(
    @Json(name = "CreatedDate")
    var createdDate: String? = null,
    @Json(name = "GasPpm")
    var gasPpm: Double? = null,
    @Json(name = "AirQuality")
    var airQuality: AirQuality? = null
)
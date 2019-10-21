package com.lcabrales.simgas.model.readings

import com.lcabrales.simgas.model.airquality.AirQuality
import com.squareup.moshi.Json

data class SensorReading(
    @Json(name = "SensorReadingId")
    var sensorReadingId: String? = null,
    @Json(name = "SensorId")
    var sensorId: String? = null,
    @Json(name = "AirQuality")
    var airQuality: AirQuality? = null,
    @Json(name = "ReadingVolts")
    var readingVolts: Double? = null,
    @Json(name = "SensorResistance")
    var sensorResistance: Double? = null,
    @Json(name = "KnownConcentrationSensorResistance")
    var knownConcentrationSensorResistance: Double? = null,
    @Json(name = "GasPpm")
    var gasPpm: Double? = null,
    @Json(name = "GasPercentage")
    var gasPercentage: Double? = null,
    @Json(name = "CreatedDate")
    var createdDate: String? = null
)
package com.lcabrales.simgas.model.readings

import com.squareup.moshi.Json

data class SensorReading(
    @Json(name = "SensorReadingId")
    var sensorReadingId: String? = null,
    @Json(name = "SensorId")
    var sensorId: String? = null,
    @Json(name = "AirQualityId")
    var airQualityId: String? = null,
    @Json(name = "ReadingVolts")
    var readingVolts: Int? = null,
    @Json(name = "SensorResistance")
    var sensorResistance: Int? = null,
    @Json(name = "KnownConcentrationSensorResistance")
    var knownConcentrationSensorResistance: Int? = null,
    @Json(name = "GasPpm")
    var gasPpm: Int? = null,
    @Json(name = "CreatedDate")
    var createdDate: String? = null
)
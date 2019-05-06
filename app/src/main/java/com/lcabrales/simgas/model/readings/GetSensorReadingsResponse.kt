package com.lcabrales.simgas.model.readings

import com.lcabrales.simgas.model.Result
import com.squareup.moshi.Json

data class GetSensorReadingsResponse(
    @Json(name = "Data")
    var data: List<SensorReading>? = null,
    @Json(name = "Result")
    var result: Result? = null
)
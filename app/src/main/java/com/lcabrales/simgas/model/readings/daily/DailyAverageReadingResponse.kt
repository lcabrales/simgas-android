package com.lcabrales.simgas.model.readings.daily

import com.lcabrales.simgas.model.Result
import com.squareup.moshi.Json

data class DailyAverageReadingResponse(
    @Json(name = "Data")
    var data: SensorDailyAverage? = null,
    @Json(name = "Result")
    var result: Result? = null
)
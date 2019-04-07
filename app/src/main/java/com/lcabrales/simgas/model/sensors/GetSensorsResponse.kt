package com.lcabrales.simgas.model.sensors

import com.lcabrales.simgas.model.Result
import com.squareup.moshi.Json

data class GetSensorsResponse(
        @Json(name = "Data")
        var data: List<Sensor>? = null,
        @Json(name = "Result")
        var result: Result? = null
)
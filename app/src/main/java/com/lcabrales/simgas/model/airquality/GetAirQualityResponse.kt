package com.lcabrales.simgas.model.airquality

import com.lcabrales.simgas.model.Result
import com.squareup.moshi.Json

data class GetAirQualityResponse(
    @Json(name = "Data")
    var data: List<AirQuality>? = null,
    @Json(name = "Result")
    var result: Result? = null
)
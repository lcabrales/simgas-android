package com.lcabrales.simgas.model.airquality

import com.squareup.moshi.Json

data class AirQuality(
    @Json(name = "AirQualityId")
    var airQualityId: String? = null,
    @Json(name = "Name")
    var name: String? = null,
    @Json(name = "ShortDescription")
    var shortDescription: String? = null,
    @Json(name = "FullDescription")
    var fullDescription: String? = null,
    @Json(name = "MinValue")
    var minValue: Int? = null,
    @Json(name = "MaxValue")
    var maxValue: Int? = null,
    @Json(name = "PrimaryColor")
    var primaryColor: String? = null,
    @Json(name = "SecondaryColor")
    var secondaryColor: String? = null,
    @Json(name = "SortOrder")
    var sortOrder: Int? = null
)
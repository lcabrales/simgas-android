package com.lcabrales.simgas.model.gas

import com.squareup.moshi.Json

data class Gas(
    @Json(name = "GasId")
    var gasId: String? = null,
    @Json(name = "Name")
    var name: String? = null,
    @Json(name = "ShortDescription")
    var shortDescription: String? = null,
    @Json(name = "FullDescription")
    var fullDescription: String? = null,
    @Json(name = "PrimaryColor")
    var primaryColor: String? = null,
    @Json(name = "SecondaryColor")
    var secondaryColor: String? = null
)
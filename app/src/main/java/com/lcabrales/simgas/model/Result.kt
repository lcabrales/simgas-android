package com.lcabrales.simgas.model

import com.squareup.moshi.Json

data class Result(
        @Json(name = "Code")
        var code: Int? = null,
        @Json(name = "Message")
        var message: String? = null
)
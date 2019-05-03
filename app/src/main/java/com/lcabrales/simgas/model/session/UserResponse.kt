package com.lcabrales.simgas.model.session

import com.lcabrales.simgas.model.Result
import com.squareup.moshi.Json

data class UserResponse (
    @Json(name = "Data")
    var user: User? = null,
    @Json(name = "Result")
    var result: Result? = null
)
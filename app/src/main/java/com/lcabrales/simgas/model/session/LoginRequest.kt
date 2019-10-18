package com.lcabrales.simgas.model.session

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "Username")
    var username: String? = null,
    @Json(name = "Password")
    var password: String? = null
)
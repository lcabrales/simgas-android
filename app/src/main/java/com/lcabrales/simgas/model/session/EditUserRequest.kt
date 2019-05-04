package com.lcabrales.simgas.model.session

import com.squareup.moshi.Json

data class EditUserRequest(
    @Json(name = "UserId")
    var userId: String? = null,
    @Json(name = "FirstName")
    var firstName: String? = null,
    @Json(name = "LastName")
    var lastName: String? = null,
    @Json(name = "Email")
    var email: String? = null
)
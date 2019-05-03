package com.lcabrales.simgas.model.session

import com.squareup.moshi.Json

data class RegisterRequest(
    @Json(name = "RoleId")
    var roleId: String? = null,
    @Json(name = "Username")
    var username: String? = null,
    @Json(name = "FirstName")
    var firstName: String? = null,
    @Json(name = "LastName")
    var lastName: String? = null,
    @Json(name = "Email")
    var email: String? = null,
    @Json(name = "Password")
    var password: String? = null
)
package com.lcabrales.simgas.model.session

import com.squareup.moshi.Json

data class User (
    @Json(name = "UserId")
    var userId: String? = null,
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
    @Json(name = "CreatedDate")
    var createdDate: String? = null
)
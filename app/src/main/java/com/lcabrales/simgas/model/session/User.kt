package com.lcabrales.simgas.model.session

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class User(
    @field:PrimaryKey
    @Json(name = "UserId")
    var userId: String,
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
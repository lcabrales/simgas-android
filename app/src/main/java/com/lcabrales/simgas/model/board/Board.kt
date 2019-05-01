package com.lcabrales.simgas.model.board

import com.squareup.moshi.Json

data class Board(
        @Json(name = "BoardId")
        var boardId: String? = null,
        @Json(name = "LocationId")
        var locationId: String? = null,
        @Json(name = "Name")
        var name: String? = null,
        @Json(name = "ShortDescription")
        var shortDescription: String? = null,
        @Json(name = "FullDescription")
        var fullDescription: String? = null
)
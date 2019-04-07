package com.lcabrales.simgas.model.sensors

import com.squareup.moshi.Json

data class Sensor(
        @Json(name = "SensorId")
        var sensorId: String? = null,
        @Json(name = "BoardId")
        var boardId: String? = null,
        @Json(name = "GasId")
        var gasId: String? = null,
        @Json(name = "Name")
        var name: String? = null,
        @Json(name = "ShortDescription")
        var shortDescription: Any? = null,
        @Json(name = "FullDescription")
        var fullDescription: Any? = null,
        @Json(name = "LoadResistance")
        var loadResistance: Int? = null,
        @Json(name = "ReceivedVolts")
        var receivedVolts: Int? = null,
        @Json(name = "Status")
        var status: Boolean? = null
)
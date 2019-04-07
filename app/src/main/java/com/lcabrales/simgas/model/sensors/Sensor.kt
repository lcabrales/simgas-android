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
) {

    fun withSensorId(sensorId: String): Sensor {
        this.sensorId = sensorId
        return this
    }

    fun withBoardId(boardId: String): Sensor {
        this.boardId = boardId
        return this
    }

    fun withGasId(gasId: String): Sensor {
        this.gasId = gasId
        return this
    }

    fun withName(name: String): Sensor {
        this.name = name
        return this
    }

    fun withShortDescription(shortDescription: Any): Sensor {
        this.shortDescription = shortDescription
        return this
    }

    fun withFullDescription(fullDescription: Any): Sensor {
        this.fullDescription = fullDescription
        return this
    }

    fun withLoadResistance(loadResistance: Int?): Sensor {
        this.loadResistance = loadResistance
        return this
    }

    fun withReceivedVolts(receivedVolts: Int?): Sensor {
        this.receivedVolts = receivedVolts
        return this
    }

    fun withStatus(status: Boolean?): Sensor {
        this.status = status
        return this
    }
}
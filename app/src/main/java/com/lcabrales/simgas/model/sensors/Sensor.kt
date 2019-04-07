package com.lcabrales.simgas.model.sensors

import com.lcabrales.simgas.model.board.Board
import com.lcabrales.simgas.model.gas.Gas
import com.lcabrales.simgas.model.readings.SensorReading
import com.squareup.moshi.Json

data class Sensor(
    @Json(name = "SensorId")
    var sensorId: String? = null,
    @Json(name = "Name")
    var name: String? = null,
    @Json(name = "ShortDescription")
    var shortDescription: String? = null,
    @Json(name = "FullDescription")
    var fullDescription: String? = null,
    @Json(name = "LoadResistance")
    var loadResistance: Int? = null,
    @Json(name = "ReceivedVolts")
    var receivedVolts: Int? = null,
    @Json(name = "LastSensorReading")
    var lastSensorReading: SensorReading? = null,
    @Json(name = "Board")
    var board: Board? = null,
    @Json(name = "Gas")
    var gas: Gas? = null
)
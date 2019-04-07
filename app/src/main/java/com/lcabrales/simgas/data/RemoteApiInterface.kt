package com.lcabrales.simgas.data

import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 */
interface RemoteApiInterface {

    /**
     * Get the list of the Sensors from the remote API
     */
    @GET("/Sensor")
    fun getSensors(): Observable<GetSensorsResponse>
}
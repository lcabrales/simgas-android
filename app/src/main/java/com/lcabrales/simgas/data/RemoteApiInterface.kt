package com.lcabrales.simgas.data

import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import com.lcabrales.simgas.model.session.LoginRequest
import com.lcabrales.simgas.model.session.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * The interface which provides methods to get result of webservices
 */
interface RemoteApiInterface {

    /**
     * Get the list of the Sensors from the remote API
     */
    @POST("/Session/login")
    fun login(@Body request: LoginRequest): Observable<LoginResponse>

    /**
     * Get the list of the Sensors from the remote API
     */
    @GET("/Sensor")
    fun getSensors(): Observable<GetSensorsResponse>
}
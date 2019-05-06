package com.lcabrales.simgas.data

import com.lcabrales.simgas.model.readings.daily.DailyAverageReadingResponse
import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import com.lcabrales.simgas.model.session.EditUserRequest
import com.lcabrales.simgas.model.session.LoginRequest
import com.lcabrales.simgas.model.session.RegisterRequest
import com.lcabrales.simgas.model.session.UserResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface RemoteApiInterface {

    /**
     * Perform a user login attempt.
     */
    @POST("/Session/login")
    fun login(@Body request: LoginRequest): Observable<UserResponse>

    /**
     * Create a new user account.
     */
    @POST("/User")
    fun register(@Body request: RegisterRequest): Observable<UserResponse>

    /**
     * Edit a user account.
     */
    @PUT("/User")
    fun editUser(@Body request: EditUserRequest): Observable<UserResponse>

    /**
     * Get the list of the Sensors from the remote API
     */
    @GET("/Sensor")
    fun getSensors(): Observable<GetSensorsResponse>

    /**
     * Get a specific sensor from the remote API.
     */
    @GET("/Sensor?")
    fun getSensorDetail(@Query("SensorId") sensorId: String): Observable<GetSensorsResponse>

    /**
     * Get the daily average readings for a specific sensor.
     */
    @GET("/SensorReading/Daily/SensorId/{SensorId}")
    fun getSensorDailyAverageReading(@Path("SensorId") sensorId: String, @Query(
        "StartDate") startDate: String): Observable<DailyAverageReadingResponse>

}
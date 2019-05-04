package com.lcabrales.simgas.data

import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import com.lcabrales.simgas.model.session.EditUserRequest
import com.lcabrales.simgas.model.session.LoginRequest
import com.lcabrales.simgas.model.session.RegisterRequest
import com.lcabrales.simgas.model.session.UserResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

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
}
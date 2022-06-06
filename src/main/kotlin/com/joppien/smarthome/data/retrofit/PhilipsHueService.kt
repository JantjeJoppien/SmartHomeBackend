package com.joppien.smarthome.data.retrofit

import com.joppien.smarthome.data.retrofit.models.HueLightResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PhilipsHueService {

    @GET("/resource/light")
    fun getLightList(): Call<List<HueLightResponse>>

    @GET("/resource/light/{id}")
    fun getLight(@Path("id") id: Int): Call<HueLightResponse>

    @PUT("/resource/light/{id}")
    fun updateLight(@Path("id") id: Int, @Body hueLightResponse: HueLightResponse)

}
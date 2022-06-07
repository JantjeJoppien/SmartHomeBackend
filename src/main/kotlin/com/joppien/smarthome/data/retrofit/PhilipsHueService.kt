package com.joppien.smarthome.data.retrofit

import com.joppien.smarthome.data.retrofit.models.HueLightListResponse
import com.joppien.smarthome.data.retrofit.models.HueLightRequest
import com.joppien.smarthome.data.retrofit.models.HueLightResponse
import retrofit2.Call
import retrofit2.http.*

interface PhilipsHueService {

    @GET("./resource/light")
    fun getLightList(): Call<HueLightListResponse>

    @GET("./resource/light/{id}")
    fun getLight(@Path("id") id: String): Call<HueLightResponse>

    @PUT("./resource/light/{id}")
    fun updateLight(@Path("id") id: String, @Body hueLightRequest: HueLightRequest)

}
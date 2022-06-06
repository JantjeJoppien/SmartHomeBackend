package com.joppien.smarthome.data.retrofit

import com.joppien.smarthome.data.utils.Bridge
import retrofit2.Call
import retrofit2.http.GET

interface PhilipsBridgeService {
    @GET(".")
    fun getBridgeIp(): Call<List<Bridge>>
}
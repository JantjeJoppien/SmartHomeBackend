package com.joppien.smarthome.data.managers

import com.joppien.smarthome.data.retrofit.PhilipsHueService
import com.joppien.smarthome.data.utils.Bridge
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component
class HueLightManager {

    @Autowired
    lateinit var bridge: Bridge

    val service: PhilipsHueService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://${bridge.ipAddress}")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        retrofit.create(PhilipsHueService::class.java)
    }

    fun getLightList(): List<String> {
        val result = service.getLightList().execute()

    }

}
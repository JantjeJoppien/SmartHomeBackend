package com.joppien.smarthome.data.managers

import com.joppien.smarthome.data.retrofit.PhilipsHueService
import com.joppien.smarthome.data.utils.Bridge
import com.joppien.smarthome.rest.models.LightResponse
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component
class HueLightManager {

    val bridge: Bridge by lazy { Bridge.initBridge() }

    private val logger by lazy { LoggerFactory.getLogger(HueLightManager::class.java) }

    val service: PhilipsHueService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://${bridge.ipAddress}")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        retrofit.create(PhilipsHueService::class.java)
    }

    fun getLightList(): List<LightResponse> {
        val result = service.getLightList().execute()
        return if (result.isSuccessful) {
            result.body()?.map { LightResponse(it) } ?: emptyList()
        } else {
            logger.error("Error while retrieving light list")
            emptyList()
        }
    }

}
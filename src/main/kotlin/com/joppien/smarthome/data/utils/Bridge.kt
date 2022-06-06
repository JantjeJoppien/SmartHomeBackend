package com.joppien.smarthome.data.utils

import com.google.gson.annotations.SerializedName
import com.joppien.smarthome.data.retrofit.PhilipsBridgeService
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

data class Bridge(
    @SerializedName("internalipaddress")
    var ipAddress: String
) {

    companion object {
        private var errorCount = 0

        private val logger by lazy {
            LoggerFactory.getLogger(Bridge::class.java)
        }

        fun initBridge(): Bridge {
            val service = getService()
            while (errorCount < 3) {
                try {
                    val result = performCall(service)
                    if (result.isSuccessful && result.body() != null) return result.body()!!.first()
                    else onCallError()
                } catch (exception: Exception) {
                    onCallError(exception)
                }
            }
            throw Exception("Unable to retrieve bridge data")
        }

        private fun getService(): PhilipsBridgeService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://discovery.meethue.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()

            return retrofit.create(PhilipsBridgeService::class.java)
        }

        private fun performCall(service: PhilipsBridgeService) = service.getBridgeIp().execute()

        private fun onCallError(exception: Exception? = null) {
            logger.error("Error while performing bridge call; errorCount = $errorCount; exception = $exception")
            errorCount++
        }
    }

}
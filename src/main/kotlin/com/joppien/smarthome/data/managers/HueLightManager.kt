package com.joppien.smarthome.data.managers

import com.joppien.smarthome.data.retrofit.PhilipsHueService
import com.joppien.smarthome.data.retrofit.models.HueLightRequest
import com.joppien.smarthome.data.utils.Bridge
import com.joppien.smarthome.data.utils.DeviceType
import com.joppien.smarthome.rest.models.LightMetadataResponse
import com.joppien.smarthome.rest.models.LightRequest
import com.joppien.smarthome.rest.models.LightResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


@Component
class HueLightManager {

    companion object {
        private val REQUEST_HEADER_KEY = "hue-application-key"
    }

    @Value("\${secrets.hue}")
    lateinit var requestHeaderValue: String

    val bridge: Bridge by lazy { Bridge.initBridge() }

    private val logger by lazy { LoggerFactory.getLogger(HueLightManager::class.java) }

    val service: PhilipsHueService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://192.168.178.57/clip/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()
        retrofit.create(PhilipsHueService::class.java)
    }

    fun getAllLightMetadata(): List<LightMetadataResponse> {
        val result = service.getLightList().execute()
        return if (result.isSuccessful) {
            result.body()?.hueLightList?.map { LightMetadataResponse(it) } ?: emptyList()
        } else {
            logger.error("Error while retrieving light list")
            emptyList()
        }
    }

    fun getLightList(idList: List<String?>): List<LightResponse> {
        val result = service.getLightList().execute()
        return if (result.isSuccessful) {
            val lightResponseList = result.body()?.hueLightList?.map { LightResponse(it) } ?: emptyList()
            // Only return data of configured devices
            lightResponseList.filter { response -> idList.any { response.id == it } }
        } else {
            logger.error("Error while retrieving light list")
            emptyList()
        }
    }

    fun getLightData(id: String): LightResponse {
        val result = service.getLight(id).execute()
        return if (result.isSuccessful) {
            result.body()?.let { LightResponse(it) } ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Device not available"
            )
        } else {
            logger.error("Error while retrieving light data")
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Device not available"
            )
        }
    }

    fun setLightData(id: String, lightRequest: LightRequest) {
        when (lightRequest.deviceType) {
            DeviceType.PHILIPS_HUE.id -> service.updateLight(id, HueLightRequest(lightRequest))
            else -> {
                logger.error("Error while setting light data")
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Device not available"
                )
            }
        }
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }

            val trustManager =
                trustManagers[0] as X509TrustManager


            OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder().addHeader(REQUEST_HEADER_KEY, requestHeaderValue).build()
                    chain.proceed(request)
                })
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}
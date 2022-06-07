package com.joppien.smarthome.data.managers

import com.joppien.smarthome.data.repositories.LightRepository
import com.joppien.smarthome.data.repositories.models.LightModel
import com.joppien.smarthome.data.retrofit.PhilipsHueService
import com.joppien.smarthome.data.retrofit.models.HueLightRequest
import com.joppien.smarthome.data.utils.Bridge
import com.joppien.smarthome.data.utils.DeviceType
import com.joppien.smarthome.rest.models.LightMetadataRequest
import com.joppien.smarthome.rest.models.LightMetadataResponse
import com.joppien.smarthome.rest.models.LightRequest
import com.joppien.smarthome.rest.models.LightResponse
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import retrofit2.HttpException
import retrofit2.Response
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

    @Autowired
    private lateinit var lightRepository: LightRepository

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

    fun getLightList(): List<LightResponse> {
        val result = service.getLightList().execute()
        val lightMetadata = lightRepository.findAll()
        return if (result.isSuccessful) {
            val lightResponseList = result.body()?.hueLightList?.map { LightResponse(it) } ?: emptyList()
            // Only return data of configured devices
            lightResponseList.filter { response -> lightMetadata.any { response.id == it?.interfaceId } }
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

    fun setLightMetaData(lightMetadataRequest: LightMetadataRequest) {
        lightRepository.save(LightModel(lightMetadataRequest))
    }

    fun setLightData(lightRequest: LightRequest) {
        when (lightRequest.deviceType) {
            DeviceType.PHILIPS_HUE.id -> service.updateLight(lightRequest.id, HueLightRequest(lightRequest))
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
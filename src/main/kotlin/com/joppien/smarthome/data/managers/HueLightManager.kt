package com.joppien.smarthome.data.managers

import com.joppien.smarthome.data.repositories.LightRepository
import com.joppien.smarthome.data.retrofit.PhilipsHueService
import com.joppien.smarthome.data.utils.Bridge
import com.joppien.smarthome.rest.models.LightResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
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
            .baseUrl("https://${bridge.ipAddress}")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()
        retrofit.create(PhilipsHueService::class.java)
    }

    fun getLightList(): List<LightResponse> {
        val result = service.getLightList().execute()
        return if (result.isSuccessful) {
            result.body()?.hueLightList?.map { LightResponse(it) } ?: emptyList()
        } else {
            logger.error("Error while retrieving light list")
            emptyList()
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
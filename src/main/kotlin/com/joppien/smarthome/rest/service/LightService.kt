package com.joppien.smarthome.rest.service

import com.joppien.smarthome.data.managers.HueLightManager
import com.joppien.smarthome.data.utils.DeviceType
import com.joppien.smarthome.rest.models.LightMetadataRequest
import com.joppien.smarthome.rest.models.LightRequest
import com.joppien.smarthome.rest.models.LightResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class LightService {

    private val logger by lazy { LoggerFactory.getLogger(LightService::class.java) }

    @Autowired
    lateinit var hueLightManager: HueLightManager

    fun getLightList() = hueLightManager.getLightList()

    fun getAllLightMetadata() = hueLightManager.getAllLightMetadata()

    fun getLightData(id: String): LightResponse {
        return hueLightManager.getLightData(id)
    }

    fun setLightMetaData(lightMetadataRequest: LightMetadataRequest) {
        when (lightMetadataRequest.deviceType) {
            DeviceType.PHILIPS_HUE.id -> hueLightManager.setLightMetaData(lightMetadataRequest)
        }
    }

    fun setLightData(lightRequest: LightRequest) {
        when (lightRequest.deviceType) {
            DeviceType.PHILIPS_HUE.id -> hueLightManager.setLightData(lightRequest)
            else -> {
                logger.error("Error while setting light data")
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Device not available"
                )
            }
        }
    }

}
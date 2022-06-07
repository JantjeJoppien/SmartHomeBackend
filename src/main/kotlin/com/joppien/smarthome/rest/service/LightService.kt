package com.joppien.smarthome.rest.service

import com.joppien.smarthome.data.managers.HomeManager
import com.joppien.smarthome.data.managers.HueLightManager
import com.joppien.smarthome.data.managers.LightMetadataManager
import com.joppien.smarthome.data.utils.DeviceType
import com.joppien.smarthome.rest.models.LightMetadataRequest
import com.joppien.smarthome.rest.models.LightMetadataResponse
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
    lateinit var lightMetadataManager: LightMetadataManager

    @Autowired
    lateinit var hueLightManager: HueLightManager

    @Autowired
    lateinit var homeManager: HomeManager

    fun getAllAvailableLightMetadata(): List<LightMetadataResponse> = hueLightManager.getAllLightMetadata()

    fun setLightMetadata(metadataList: List<LightMetadataRequest>) = lightMetadataManager.setLightMetaData(metadataList)

    fun getConfiguredLightList(): List<LightResponse> {
        val configuredDeviceTypes = homeManager.getConfiguredDeviceTypeList()
        if (configuredDeviceTypes.isEmpty()) return emptyList()

        val lightMetadata = lightMetadataManager.getAllLightMetadata()
        if (lightMetadata.isEmpty()) return emptyList()

        val resultList = mutableListOf<LightResponse>()

        val hueList = if (configuredDeviceTypes.contains(DeviceType.PHILIPS_HUE.id))
            lightMetadata.filter { it?.deviceType == DeviceType.PHILIPS_HUE.id } else emptyList()
        when {
            hueList.isNotEmpty() -> {
                val hueIds = hueList.map { it?.id }
                resultList.addAll(hueLightManager.getLightList(hueIds))
            }
        }
        return resultList
    }

    fun getLightData(id: String): LightResponse {
        val metadata = lightMetadataManager.getLightData(id)
        if (metadata != null) {
            return when (metadata.deviceType) {
                DeviceType.PHILIPS_HUE.id -> hueLightManager.getLightData(id)
                else -> throw onLightNotFound(id)
            }
        } else throw onLightNotFound(id)
    }

    fun setLightData(id: String, lightRequest: LightRequest) {
        when (lightRequest.deviceType) {
            DeviceType.PHILIPS_HUE.id -> hueLightManager.setLightData(id, lightRequest)
            else -> throw onLightNotFound(id)
        }
    }

    private fun onLightNotFound(id: String): ResponseStatusException {
        logger.error("Light not found; Id = $id")
        return ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Device with id $id not found"
        )
    }

}
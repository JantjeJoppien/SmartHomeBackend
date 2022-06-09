package com.joppien.smarthome.data.managers

import com.joppien.smarthome.data.repositories.LightRepository
import com.joppien.smarthome.data.repositories.models.LightModel
import com.joppien.smarthome.rest.models.LightMetadataRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class LightMetadataManager {

    @Value("\${secrets.hue}")
    lateinit var requestHeaderValue: String

    @Autowired
    private lateinit var lightRepository: LightRepository

    fun getAllLightMetadata(): List<LightModel?> = lightRepository.findAll().toList()

    fun getLightData(id: String): LightModel? = lightRepository.findById(id).orElse(null)

    fun setLightMetaData(metadataList: List<LightMetadataRequest>) = metadataList.forEach {
        lightRepository.save(LightModel(it))
    }

    fun clearLightData() = lightRepository.deleteAll()

}
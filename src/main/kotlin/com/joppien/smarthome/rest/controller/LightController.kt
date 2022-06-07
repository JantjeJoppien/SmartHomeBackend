package com.joppien.smarthome.rest.controller

import com.joppien.smarthome.rest.models.LightMetadataRequest
import com.joppien.smarthome.rest.models.LightRequest
import com.joppien.smarthome.rest.service.LightService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import retrofit2.http.Body

@RestController
@RequestMapping("/v1/light")
class LightController {

    @Autowired
    lateinit var lightService: LightService

    @GetMapping("/configure")
    fun getLightList() = lightService.getAllAvailableLightMetadata()

    @PutMapping("/configure")
    fun setLightList(@Body metadataList: List<LightMetadataRequest>) = lightService.setLightMetadata(metadataList)

    @GetMapping
    fun getConfiguredLightList() = lightService.getConfiguredLightList()

    @GetMapping("/{id}")
    fun getLight(@PathVariable id: String) = lightService.getLightData(id)

    @GetMapping("/{id}")
    fun getLight(@PathVariable id: String, @Body lightRequest: LightRequest) =
        lightService.setLightData(id, lightRequest)
}
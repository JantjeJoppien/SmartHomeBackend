package com.joppien.smarthome.rest.controller

import com.joppien.smarthome.rest.models.LightMetadataRequest
import com.joppien.smarthome.rest.models.LightRequest
import com.joppien.smarthome.rest.service.LightService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/light")
class LightController {

    @Autowired
    lateinit var lightService: LightService

    @GetMapping("/configure")
    fun getLightList() = lightService.getAllAvailableLightMetadata()

    @PutMapping("/configure")
    fun setLightList(@RequestBody metadataList: List<LightMetadataRequest>) = lightService.setLightMetadata(metadataList)

    @DeleteMapping("/configure")
    fun clearLightList() = lightService.clearLightMetadata()

    @GetMapping
    fun getConfiguredLightList() = lightService.getConfiguredLightList()

    @GetMapping("/{id}")
    fun getLight(@PathVariable id: String) = lightService.getLightData(id)

    @PutMapping("/{id}")
    fun setLight(@PathVariable id: String, @RequestBody lightRequest: LightRequest) =
        lightService.setLightData(id, lightRequest)
}
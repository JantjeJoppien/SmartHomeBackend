package com.joppien.smarthome.rest.controller

import com.joppien.smarthome.rest.service.LightService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/light")
class LightController {

    @Autowired
    lateinit var lightService: LightService

    @GetMapping
    fun getLightList() = lightService.getLightList()
}
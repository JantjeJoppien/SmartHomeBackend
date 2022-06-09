package com.joppien.smarthome.rest.controller

import com.joppien.smarthome.rest.models.HomeRequest
import com.joppien.smarthome.rest.service.HomeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/home")
class HomeController {

    @Autowired
    lateinit var homeService: HomeService

    @GetMapping
    fun getDeviceTypes(): HomeRequest = homeService.getConfiguredDeviceTypes()

    @PutMapping
    fun setDeviceTypes(@RequestBody homeRequest: HomeRequest) = homeService.setConfiguredDeviceTypes(homeRequest)

}
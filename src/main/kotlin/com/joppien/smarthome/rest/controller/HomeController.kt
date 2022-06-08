package com.joppien.smarthome.rest.controller

import com.joppien.smarthome.rest.models.HomeRequest
import com.joppien.smarthome.rest.service.HomeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import retrofit2.http.Body


@RestController
@RequestMapping("/v1/home")
class HomeController {

    @Autowired
    lateinit var homeService: HomeService

    @PutMapping
    fun setDeviceTypes(@Body homeRequest: HomeRequest) = homeService.setConfiguredDeviceTypes(homeRequest)

}
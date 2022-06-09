package com.joppien.smarthome.rest.service

import com.joppien.smarthome.data.managers.HomeManager
import com.joppien.smarthome.rest.models.HomeRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HomeService {

    @Autowired
    lateinit var homeManager: HomeManager

    fun getConfiguredDeviceTypes(): HomeRequest = homeManager.getConfiguredDeviceType()

    fun setConfiguredDeviceTypes(homeRequest: HomeRequest) = homeManager.setConfiguredDeviceTypeList(homeRequest)

}
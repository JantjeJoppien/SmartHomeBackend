package com.joppien.smarthome.rest.service

import com.joppien.smarthome.data.managers.HueLightManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LightService {

    @Autowired
    lateinit var hueLightManager: HueLightManager

    fun getLightList() = hueLightManager.getLightList()

}
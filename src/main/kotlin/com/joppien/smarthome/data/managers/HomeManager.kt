package com.joppien.smarthome.data.managers

import com.joppien.smarthome.data.repositories.HomeRepository
import com.joppien.smarthome.data.repositories.models.HomeModel
import com.joppien.smarthome.data.utils.DeviceType
import com.joppien.smarthome.rest.models.HomeRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HomeManager {

    @Autowired
    private lateinit var homeRepository: HomeRepository

    fun getConfiguredDeviceTypeList(): List<Int> = homeRepository.findAll().first()?.deviceTypeList ?: emptyList()

    fun setConfiguredDeviceTypeList(homeRequest: HomeRequest) =
        homeRepository.save(HomeModel(deviceTypeList = getDeviceTypeListFromHomeRequest(homeRequest)))

    private fun getDeviceTypeListFromHomeRequest(homeRequest: HomeRequest): List<Int> {
        val deviceTypeList = mutableListOf<Int>()
        if (homeRequest.philipsHueEnabled) deviceTypeList.add(DeviceType.PHILIPS_HUE.id)

        return deviceTypeList
    }

}
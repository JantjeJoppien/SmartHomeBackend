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

    fun getConfiguredDeviceTypeList(): List<Int> {
        val modelList = homeRepository.findAll()
        if (modelList.count() == 0) return emptyList()
        return getDeviceTypeListFromHomeModel(modelList.first())
    }

    fun setConfiguredDeviceTypeList(homeRequest: HomeRequest) =
        homeRepository.save(HomeModel(philipsHueEnabled = homeRequest.philipsHueEnabled))

    fun getConfiguredDeviceType(): HomeRequest =
        homeRepository.findAll().first()?.let { HomeRequest(it.philipsHueEnabled) } ?: HomeRequest(false)

    private fun getDeviceTypeListFromHomeModel(homeModel: HomeModel?): List<Int> {
        val deviceTypeList = mutableListOf<Int>()
        if (homeModel?.philipsHueEnabled == true) deviceTypeList.add(DeviceType.PHILIPS_HUE.id)

        return deviceTypeList
    }

}
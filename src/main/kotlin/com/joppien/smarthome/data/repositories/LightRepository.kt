package com.joppien.smarthome.data.repositories

import com.joppien.smarthome.data.repositories.models.LightModel
import org.springframework.data.repository.CrudRepository

interface LightRepository : CrudRepository<LightModel?, String?>
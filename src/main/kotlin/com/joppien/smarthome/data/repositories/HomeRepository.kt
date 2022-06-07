package com.joppien.smarthome.data.repositories

import com.joppien.smarthome.data.repositories.models.HomeModel
import org.springframework.data.repository.CrudRepository

interface HomeRepository : CrudRepository<HomeModel?, String?>
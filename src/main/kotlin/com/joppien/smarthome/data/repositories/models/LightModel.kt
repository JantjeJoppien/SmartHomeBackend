package com.joppien.smarthome.data.repositories.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class LightModel (
        @Id
val id: String = UUID.randomUUID().toString(),
        var interfaceId: Int? = null
        )
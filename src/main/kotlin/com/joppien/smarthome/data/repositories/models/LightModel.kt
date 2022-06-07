package com.joppien.smarthome.data.repositories.models

import com.joppien.smarthome.rest.models.LightMetadataRequest
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class LightModel(
    @Id
    val id: String = UUID.randomUUID().toString(),
    var interfaceId: String? = null,
    var internalName: String? = null,
    var customName: String? = null,
    var roomName: String? = null,
    var deviceType: Int = 0
) {
    constructor(lightMetadataRequest: LightMetadataRequest) : this(
        id = lightMetadataRequest.id,
        interfaceId = lightMetadataRequest.interfaceId,
        internalName = lightMetadataRequest.internalName,
        customName = lightMetadataRequest.customName,
        roomName = lightMetadataRequest.roomName,
        deviceType = lightMetadataRequest.deviceType
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as LightModel

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , interfaceId = $interfaceId , internalName = $internalName , customName = $customName )"
    }
}
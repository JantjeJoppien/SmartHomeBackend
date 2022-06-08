package com.joppien.smarthome.data.repositories.models

import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class HomeModel(
    @Id
    // We always use the same Id because currently we can only have 1 home configured at the time
    val id: Int = 1,
    var philipsHueEnabled: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as HomeModel

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , deviceTypeList = $philipsHueEnabled )"
    }
}
package com.joppien.smarthome.rest.models

import com.google.gson.annotations.SerializedName
import com.joppien.smarthome.data.repositories.models.LightModel
import com.joppien.smarthome.data.retrofit.models.*
import com.joppien.smarthome.data.utils.DeviceType
import java.util.*

data class LightMetadataResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("internalName")
    var internalName: String?,
    @SerializedName("customName")
    var customName: String?,
    @SerializedName("roomName")
    var roomName: String?,
    @SerializedName("deviceType")
    val deviceType: Int
) {
    constructor(hueLightResponse: HueLightResponse) : this(
        id = UUID.randomUUID().toString(),
        internalName = hueLightResponse.hueLightMetadata?.name,
        customName = null,
        roomName = null,
        deviceType = DeviceType.PHILIPS_HUE.id
    )
}

data class LightResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("internalName")
    var internalName: String?,
    @SerializedName("customName")
    var customName: String?,
    @SerializedName("roomName")
    var roomName: String?,
    @SerializedName("state")
    var lightState: Boolean,
    @SerializedName("brightness")
    var brightness: Float?,
    @SerializedName("color")
    var lightColor: LightColor?,
    @SerializedName("deviceType")
    val deviceType: Int
) {
    constructor(hueLightResponse: HueLightResponse) : this(
        id = hueLightResponse.id,
        internalName = hueLightResponse.hueLightMetadata?.name,
        customName = null,
        roomName = null,
        lightState = hueLightResponse.hueLightState?.lightOn ?: false,
        brightness = hueLightResponse.hueLightDimmingState?.brightness,
        lightColor = hueLightResponse.hueLightColor?.let { LightColor(it) },
        deviceType = DeviceType.PHILIPS_HUE.id
    )

    fun setFromMetadata(lightModel : LightModel) {
        customName = lightModel.customName
        roomName = lightModel.roomName
    }
}

data class LightColor(
    @SerializedName("xy")
    val generalXYValues: LightColorXY,
    @SerializedName("gamut")
    val gamut: LightColorGamut?
) {
    constructor(hueLightColor: HueLightResponseColor) : this(
        generalXYValues = LightColorXY(hueLightColor.generalXYValues),
        gamut = hueLightColor.gamut?.let { LightColorGamut(it) }
    )
}

data class LightColorGamut(
    @SerializedName("red")
    val redXYValues: LightColorXY,
    @SerializedName("green")
    val greenXYValues: LightColorXY,
    @SerializedName("blue")
    val blueXYValues: LightColorXY
) {
    constructor(hueLightColorGamut: HueLightResponseColorGamut) : this(
        redXYValues = LightColorXY(hueLightColorGamut.redXYValues),
        greenXYValues = LightColorXY(hueLightColorGamut.greenXYValues),
        blueXYValues = LightColorXY(hueLightColorGamut.blueXYValues)
    )
}

data class LightColorXY(
    @SerializedName("x")
    val xValue: Float,
    @SerializedName("y")
    val yValue: Float
) {
    constructor(hueLightColorXY: HueLightResponseColorXY) : this(
        xValue = hueLightColorXY.xValue,
        yValue = hueLightColorXY.yValue
    )
}
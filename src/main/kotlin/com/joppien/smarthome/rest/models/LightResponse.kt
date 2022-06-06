package com.joppien.smarthome.rest.models

import com.google.gson.annotations.SerializedName
import com.joppien.smarthome.data.retrofit.models.HueLightColor
import com.joppien.smarthome.data.retrofit.models.HueLightColorGamut
import com.joppien.smarthome.data.retrofit.models.HueLightColorXY
import com.joppien.smarthome.data.retrofit.models.HueLightResponse

data class LightResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    var name: String?,
    @SerializedName("state")
    var lightState: Boolean,
    @SerializedName("brightness")
    var brightness: Float?,
    @SerializedName("color")
    var lightColor: LightColor?
) {
    constructor(hueLightResponse: HueLightResponse) : this(
        id = hueLightResponse.id,
        name = hueLightResponse.hueLightMetadata?.name,
        lightState = hueLightResponse.hueLightState?.lightOn ?: false,
        brightness = hueLightResponse.hueLightDimmingState?.brightness,
        lightColor = hueLightResponse.hueLightColor?.let { LightColor(it) }
    )
}

data class LightColor(
    @SerializedName("gamut_type")
    val gamutType: Char,
    @SerializedName("xy")
    val generalXYValues: LightColorXY,
    @SerializedName("gamut")
    val gamut: LightColorGamut?
) {
    constructor(hueLightColor: HueLightColor) : this(
        gamutType = hueLightColor.gamutType,
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
    constructor(hueLightColorGamut: HueLightColorGamut) : this(
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
    constructor(hueLightColorXY: HueLightColorXY) : this(
        xValue = hueLightColorXY.xValue,
        yValue = hueLightColorXY.yValue
    )
}
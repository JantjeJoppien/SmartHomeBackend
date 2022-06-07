package com.joppien.smarthome.data.retrofit.models

import com.google.gson.annotations.SerializedName
import com.joppien.smarthome.rest.models.LightColor
import com.joppien.smarthome.rest.models.LightColorGamut
import com.joppien.smarthome.rest.models.LightColorXY
import com.joppien.smarthome.rest.models.LightRequest

data class HueLightRequest(
    @SerializedName("on")
    var hueLightState: HueLightRequestState?,
    @SerializedName("dimming")
    var hueLightDimmingState: HueLightRequestDimmingState?,
    @SerializedName("color")
    var hueLightColor: HueLightRequestColor?
) {
    constructor(lightRequest: LightRequest) : this(
        hueLightState = HueLightRequestState(lightRequest.lightState),
        hueLightDimmingState = lightRequest.brightness?.let { HueLightRequestDimmingState(it) },
        hueLightColor = lightRequest.lightColor?.let { HueLightRequestColor(it) }
    )
}

data class HueLightRequestState(
    @SerializedName("on")
    var lightOn: Boolean
)

data class HueLightRequestDimmingState(
    @SerializedName("brightness")
    var brightness: Float
)

data class HueLightRequestColor(
    @SerializedName("xy")
    val generalXYValues: HueLightRequestColorXY,
    @SerializedName("gamut")
    val gamut: HueLightRequestColorGamut?,
) {
    constructor(lightColor: LightColor) : this(
        generalXYValues = HueLightRequestColorXY(lightColor.generalXYValues),
        gamut = lightColor.gamut?.let { HueLightRequestColorGamut(it) }
    )
}

data class HueLightRequestColorGamut(
    @SerializedName("red")
    val redXYValues: HueLightRequestColorXY,
    @SerializedName("green")
    val greenXYValues: HueLightRequestColorXY,
    @SerializedName("blue")
    val blueXYValues: HueLightRequestColorXY,
) {
    constructor(lightColorGamut: LightColorGamut) : this(
        redXYValues = HueLightRequestColorXY(lightColorGamut.redXYValues),
        greenXYValues = HueLightRequestColorXY(lightColorGamut.greenXYValues),
        blueXYValues = HueLightRequestColorXY(lightColorGamut.blueXYValues)
    )
}

data class HueLightRequestColorXY(
    @SerializedName("x")
    val xValue: Float,
    @SerializedName("y")
    val yValue: Float,
) {
    constructor(lightColorXY: LightColorXY) : this(
        xValue = lightColorXY.xValue,
        yValue = lightColorXY.yValue
    )
}
package com.joppien.smarthome.data.retrofit.models

import com.google.gson.annotations.SerializedName

data class HueLightListResponse(
    @SerializedName("data")
    var hueLightList: List<HueLightResponse>
)

data class HueLightResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("metadata")
    var hueLightMetadata: HueLightMetadata?,
    @SerializedName("on")
    var hueLightState: HueLightState?,
    @SerializedName("dimming")
    var hueLightDimmingState: HueLightDimmingState?,
    @SerializedName("color")
    var hueLightColor: HueLightColor?
)

data class HueLightMetadata(
    @SerializedName("name")
    var name: String
)

data class HueLightState(
    @SerializedName("on")
    var lightOn: Boolean
)

data class HueLightDimmingState(
    @SerializedName("brightness")
    var brightness: Float
)

data class HueLightColor(
    @SerializedName("xy")
    val generalXYValues: HueLightColorXY,
    @SerializedName("gamut")
    val gamut: HueLightColorGamut?,
)

data class HueLightColorGamut(
    @SerializedName("red")
    val redXYValues: HueLightColorXY,
    @SerializedName("green")
    val greenXYValues: HueLightColorXY,
    @SerializedName("blue")
    val blueXYValues: HueLightColorXY,
)

data class HueLightColorXY(
    @SerializedName("x")
    val xValue: Float,
    @SerializedName("y")
    val yValue: Float,
)
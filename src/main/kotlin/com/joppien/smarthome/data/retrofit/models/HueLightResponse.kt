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
    var hueLightMetadata: HueLightResponseMetadata?,
    @SerializedName("on")
    var hueLightState: HueLightResponseState?,
    @SerializedName("dimming")
    var hueLightDimmingState: HueLightResponseDimmingState?,
    @SerializedName("color")
    var hueLightColor: HueLightResponseColor?
)

data class HueLightResponseMetadata(
    @SerializedName("name")
    var name: String
)

data class HueLightResponseState(
    @SerializedName("on")
    var lightOn: Boolean
)

data class HueLightResponseDimmingState(
    @SerializedName("brightness")
    var brightness: Float
)

data class HueLightResponseColor(
    @SerializedName("xy")
    val generalXYValues: HueLightResponseColorXY,
    @SerializedName("gamut")
    val gamut: HueLightResponseColorGamut?,
)

data class HueLightResponseColorGamut(
    @SerializedName("red")
    val redXYValues: HueLightResponseColorXY,
    @SerializedName("green")
    val greenXYValues: HueLightResponseColorXY,
    @SerializedName("blue")
    val blueXYValues: HueLightResponseColorXY,
)

data class HueLightResponseColorXY(
    @SerializedName("x")
    val xValue: Float,
    @SerializedName("y")
    val yValue: Float,
)
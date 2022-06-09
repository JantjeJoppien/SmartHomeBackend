package com.joppien.smarthome.rest.models

import com.google.gson.annotations.SerializedName

data class LightMetadataRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("interfaceId")
    var interfaceId: String?,
    @SerializedName("internalName")
    var internalName: String?,
    @SerializedName("customName")
    var customName: String?,
    @SerializedName("roomName")
    var roomName: String?,
    @SerializedName("deviceType")
    val deviceType: Int
)

data class LightRequest(
    @SerializedName("state")
    var lightState: Boolean,
    @SerializedName("brightness")
    var brightness: Float?,
    @SerializedName("color")
    var lightColor: LightColor?
)
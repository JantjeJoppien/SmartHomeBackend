package com.joppien.smarthome.rest.models

import com.google.gson.annotations.SerializedName

data class HomeRequest(
    @SerializedName("philipsHueEnabled")
    var philipsHueEnabled: Boolean
)
package com.example.smarthome.ui.doorLock.data

import com.google.gson.annotations.SerializedName

data class DoorlockControlResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<DoorlockControl>
)

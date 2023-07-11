package com.example.smarthome.ui.tempHumi.data.humi

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("place")
    val place: String,
    @SerializedName("value")
    val value: Double,
)

data class StandardHumi(
    @SerializedName("standard_humi")
    val standard_humi: Double
)


data class DehumidifierControl(
    @SerializedName("dehumidifiercontrol")
    val control: Boolean
)

data class HumidifierControl (
    @SerializedName("humidifiercontrol")
    val control: Boolean
)
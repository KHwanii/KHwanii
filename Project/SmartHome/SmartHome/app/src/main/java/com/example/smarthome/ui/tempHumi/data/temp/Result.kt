package com.example.smarthome.ui.tempHumi.data.temp

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

data class StandardTemp(
    @SerializedName("standard_temp")
    val standard_temp: Double
)                               // 기준온도 설정을 따로 하기위해 새로운 Data Class 정의

data class HeaterControl(
    @SerializedName("heatercontrol")
    val control: Boolean
)

data class AirconControl(
    @SerializedName("airconcontrol")
    val control: Boolean
)

data class SmartControl(
    @SerializedName("total_control")
    val control: Boolean
)
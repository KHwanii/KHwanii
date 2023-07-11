package com.example.smarthome.ui.tempHumi.data.humi

import com.google.gson.annotations.SerializedName

data class Humidity(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)


data class StandardHumiResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")    // 데이터들
    val results: List<StandardHumi>
)


data class DehumidifierControlResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")    // 데이터들
    val results: List<DehumidifierControl>
)

data class HumidifierControlResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")    // 데이터들
    val results: List<HumidifierControl>
)

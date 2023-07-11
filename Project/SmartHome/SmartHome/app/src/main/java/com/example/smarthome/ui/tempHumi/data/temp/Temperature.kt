package com.example.smarthome.ui.tempHumi.data.temp
import com.google.gson.annotations.SerializedName

data class Temperature(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)


data class StandardTempResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")    // 데이터들
    val results: List<StandardTemp>
)

data class HeaterControlResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")    // 데이터들
    val results: List<HeaterControl>
)

data class AirconControlResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")    // 데이터들
    val results: List<AirconControl>
)


data class SmartControlResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")    // 데이터들
    val results: List<SmartControl>
)
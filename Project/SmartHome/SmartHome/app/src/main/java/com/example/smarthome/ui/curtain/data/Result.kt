package com.example.smarthome.ui.curtain.data

import com.google.gson.annotations.SerializedName

data class Result(                // 받아오는 각각의 조도 데이터들
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("place")
    val place: String,
    @SerializedName("value")
    val value: Double
)

data class CurtainControl(                    // 받아오는 각각의 커튼제어상태 데이터들
    @SerializedName("curtaincontrol")
    val control: Boolean
)

data class CurtainTime(                    // 받아오는 각각의 커튼제어상태 데이터들
    @SerializedName("time_day")
    val time_day: String,
    @SerializedName("time_night")
    val time_night: String
)


package com.example.smarthome.ui.smartCam.data


import com.google.gson.annotations.SerializedName

data class Result(   // 하나의 이미지 파일
    @SerializedName("file_name")
    val fileName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_file")
    val imgFile: String
)
package com.example.smarthome.ui.smartCam.data


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")   // http://127.0.0.1:8000/api/imgfile
    val previous: String,
    @SerializedName("results")   // 이미지 파일들
    val results: List<Result>
)


{
    fun totalPages(itemsPerPage: Int): Int {
        return (count + itemsPerPage - 1) / itemsPerPage
    }
}            // 전체 페이지수를 계산
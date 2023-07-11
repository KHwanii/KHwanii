package com.example.smarthome.ui.curtain.data

import com.google.gson.annotations.SerializedName

data class Cds(                      // http://IP주소/api/cds/ 에서 받아오는 데이터 형식
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<Result>        // results 안의 데이터가 실제 데이터. 리스트 형태로 받아서 Result 데이터 클래스에 집어넣음
)

{
    fun totalPages(itemsPerPage: Int): Int {
        return (count + itemsPerPage - 1) / itemsPerPage
    }
}            // 전체 페이지수를 계산

data class CurtainControlResponse(   // http://IP주소/api/control_curtain/ 에서 받아오는 데이터 형식
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<CurtainControl>   // results 안의 데이터가 실제 데이터. 리스트 형태로 받아서 CurtainControl 데이터 클래스에 집어넣음
)

data class CurtainTimeResponse(   // http://IP주소/api/control_curtain/ 에서 받아오는 데이터 형식
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<CurtainTime>   // results 안의 데이터가 실제 데이터. 리스트 형태로 받아서 CurtainControl 데이터 클래스에 집어넣음
)



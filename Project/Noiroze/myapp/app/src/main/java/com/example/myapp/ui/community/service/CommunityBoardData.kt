package com.example.myapp.ui.community.service

import com.google.gson.annotations.SerializedName
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.*

data class CommunityBoards(         // http://noiroze.com/api/community_board 의 JSON 데이터 형식
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")   // http://noiroze.com/api/community_board
    val previous: String,
    @SerializedName("results")   // 커뮤니티 게시판 리스트
    val results: List<Result>
)

data class Result(   // 커뮤니티 게시판 상세내용
    @SerializedName("category")
    val category: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("created_date")
    val created_date: String,
    @SerializedName("modify_date")
    val modify_date: String,
    @SerializedName("like")
    val like: List<Int>,
)
{
    val createdDate: Date
    get() {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        return format.parse(created_date) ?: Date()
    }
}             // 작성일시, 수정일시를 Date 포맷으로 변경


fun getTimeDifference(date: Date): String {
    val now = Date()
    val diffInMinutes = (now.time - date.time) / 60000

    return when {
        diffInMinutes < 60 -> "$diffInMinutes 분 전"
        diffInMinutes < 1440 -> "${diffInMinutes / 60} 시간 전"
        else -> "${diffInMinutes / 1440} 일 전"
    }
}             // 작성일시, 수정일시를 현재 시간과의 차이로 표시하기 위한 함수


data class CreatePostRequest(
    val category: String,
    val title: String,
    val content: String,
    val author: String,
    val created_date: String,
)
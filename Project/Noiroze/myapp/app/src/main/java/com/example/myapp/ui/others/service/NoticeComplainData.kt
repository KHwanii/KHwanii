package com.example.myapp.ui.others.service

import com.google.gson.annotations.SerializedName

data class ComplainBoards(         // http://noiroze.com/api/community_board 의 JSON 데이터 형식
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")   // http://noiroze.com/api/community_board
    val previous: String,
    @SerializedName("results")   // 커뮤니티 게시판 리스트
    val results: List<ComplainResult>
)

data class ComplainResult(   // 커뮤니티 게시판 상세내용
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("created_date")
    val created_date: String,
)

data class CreateComplainRequest(
    val title: String,
    val content: String,
    val author: String,
    val created_date: String,
)

data class NoticeBoards (
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")   // http://noiroze.com/api/community_board
    val previous: String,
    @SerializedName("results")   // 커뮤니티 게시판 리스트
    val results: List<NoticeResult>
)

data class NoticeResult (
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("created_date")
    val created_date: String
)
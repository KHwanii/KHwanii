package com.example.myapp.ui.decibel.service


import com.google.gson.annotations.SerializedName
import java.io.File

data class SoundLevel (
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")   // http://noiroze.com/api/community_board
    val previous: String?,
    @SerializedName("results")   // 커뮤니티 게시판 리스트
    val results: List<SLResult>
)

data class SLResult (
    @SerializedName("dong")
    val dong: String,
    @SerializedName("ho")
    val ho: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("created_at")
    val created_at: String,
)


data class SoundVerified (
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")   // http://noiroze.com/api/community_board
    val previous: String,
    @SerializedName("results")   // 커뮤니티 게시판 리스트
    val results: List<SVResult>
)


data class SVResult (
    @SerializedName("dong")
    val dong: String,
    @SerializedName("ho")
    val ho: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("sound_type")
    val sound_type: String,
)

data class SoundFile (
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")   // http://noiroze.com/api/community_board
    val previous: String,
    @SerializedName("results")   // 커뮤니티 게시판 리스트
    val results: List<SFResult>
)

data class SFResult (
    @SerializedName("dong")
    val dong: String,
    @SerializedName("ho")
    val ho: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("file_name")
    val file_name: String,
    @SerializedName("sound_file")
    val sound_file: String,
    @SerializedName("created_at")
    val created_at: String,
)
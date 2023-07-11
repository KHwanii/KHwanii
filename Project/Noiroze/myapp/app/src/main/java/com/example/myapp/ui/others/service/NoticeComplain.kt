package com.example.myapp.ui.others.service

import com.example.myapp.login.Login.client
import com.example.myapp.login.URL
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

object NoticeComplain {
    private val retrofit = Retrofit.Builder()    // retrofit 인스턴스 생성
        .baseUrl(URL)  //  API 서버가 실행 중인 컴퓨터의 IP 주소를 기본 url로 지정
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())    // json을 java 객체로 변환하기 위한 Gson
        .build()

    val noticeservice get() = retrofit.create(NoticeBoardService::class.java)
    val compservice get() = retrofit.create(ComplainBoardService::class.java)
}

interface ComplainBoardService {
    @GET("/api/complain_board/")        // retrofit의 get 이노테이션 사용- GET 요청
    fun requestComplainList(@Header("Authorization") token: String, @Query("page") page: Int): Call<ComplainBoards>

    @POST("/api/complain_board/")
    fun createComplain(@Header("Authorization") token: String, @Body request: CreateComplainRequest) : Call<ResponseBody>
}

interface NoticeBoardService {
    @GET("/api/notice_board/")
    fun requestNoticeList(@Query("page") page: Int) : Call<NoticeBoards>
}
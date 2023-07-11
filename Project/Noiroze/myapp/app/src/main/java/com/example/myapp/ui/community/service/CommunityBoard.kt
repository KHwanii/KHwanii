package com.example.myapp.ui.community.service

import com.example.myapp.login.Login.client
import com.example.myapp.login.URL
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

object CommunityBoardSetup {                 // Retrofit 사용을 위한 객체 생성 및 설정.
    private val retrofit = Retrofit.Builder()    // retrofit 인스턴스 생성
        .baseUrl(URL)  //  API 서버가 실행 중인 컴퓨터의 IP 주소를 기본 url로 지정
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())    // json을 java 객체로 변환하기 위한 Gson
        .build()

    val service get() = retrofit.create(CommunityBoardService::class.java)  // CommunityBoardService 인터페이스를 구현한 서비스 객체를 생성하는 프로퍼티
}

interface CommunityBoardService {     // RESTful API 요청을 정의하는 인터페이스
    @GET("/api/community_board/")        // retrofit의 get 이노테이션 사용- GET 요청
    fun requestCommunityBoardList(@Query("page") page: Int): Call<CommunityBoards>  // page라는 쿼리 매개변수를 받아서 게시판 목록을 가져오는 API 요청을 정의

    @POST("/api/community_board/") // 실제 API 엔드포인트에 맞게 변경해야 합니다.
    fun createPost(@Body request: CreatePostRequest): Call<ResponseBody>
}




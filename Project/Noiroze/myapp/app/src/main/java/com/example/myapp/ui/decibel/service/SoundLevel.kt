package com.example.myapp.ui.decibel.service


import com.example.myapp.login.Login.client
import com.example.myapp.login.URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

object SoundLevelSetup {
    private val retrofit = Retrofit.Builder()    // retrofit 인스턴스 생성
        .baseUrl(URL)  //  API 서버가 실행 중인 컴퓨터의 IP 주소를 기본 url로 지정
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())    // json을 java 객체로 변환하기 위한 Gson
        .build()

    val service get() = retrofit.create(SoundLevelService::class.java)
}

interface SoundLevelService {
    @GET("/api/sound_level/")        // retrofit의 get 이노테이션 사용- GET 요청
    fun getSoundLevelHome(
        @Header("Authorization") token: String,
        @Query("date") date: String?,
        @Query("page") page: Int
    ) : Call<SoundLevel>

    @GET("/api/sound_level/")        // retrofit의 get 이노테이션 사용- GET 요청
    fun getSoundLevelAll(@Query("page") page: Int) : Call<SoundLevel>

    @GET("/api/sound_level/")
    fun getSoundLevelDong(
        @Query("dong") dong: String,
        @Query("date") date: String?,
        @Query("page") page: Int
    ) : Call<SoundLevel>

    @GET("/api/sound_verified/")        // retrofit의 get 이노테이션 사용- GET 요청
    fun getSoundLevelVerified(@Header("Authorization") token: String, @Query("page") page: Int) : Call<SoundVerified>

    @GET("/api/sound_file/")
    fun getSoundRecordFile(@Header("Authorization") token: String, @Query("page") page: Int) : Call<SoundFile>
}
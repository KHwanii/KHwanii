package com.example.smarthome.ui.tempHumi.data.temp.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TEMP {    // 객체 - Retrofit 인스턴스를 생성하고 설정

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY     // HttpLoggingInterceptor 인스턴스를 생성하고 로깅 레벨을 설정
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(logging)        // OkHttpClient 인스턴스를 생성하고 HttpLoggingInterceptor를 추가
    }.build()

    private val retrofit = Retrofit.Builder()    // retrofit 인스턴스 생성
        .baseUrl("http://192.168.45.155:8000")  //  API 서버가 실행 중인 컴퓨터의 IP 주소를 기본 url로 지정
        .addConverterFactory(GsonConverterFactory.create())   // json을 java 객체로 변환하기 위한 Gson
        .client(client)  // 위에서 만든 OkHttpClient 인스턴스를 사용하여 HTTP관련 로그 출력 (ex. API 통신 로그)
        .build()

    val service get() = retrofit.create(TempService::class.java)
    // TempService 인터페이스를 구현한 서비스 객체를 생성하는 프로퍼티
}
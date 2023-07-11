package com.example.smarthome.ui.curtain.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Doorlock {    // 객체 - Retrofit 인스턴스를 생성하고 설정
    private val retrofit = Retrofit.Builder()    // retrofit 인스턴스 생성
        .baseUrl("http://192.168.45.155:8000")  //  API 서버가 실행 중인 컴퓨터의 IP 주소를 기본 url로 지정
        .addConverterFactory(GsonConverterFactory.create())
        // json을 java 객체로 변환하기 위한 Gson
        .build()

    val service get() = retrofit.create(DoorlockService::class.java)
    // DoorlockService 인터페이스를 구현한 서비스 객체를 생성하는 프로퍼티
}
package com.example.smarthome.ui.smartCam.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Img 객체는 Retrofit 인스턴스를 생성하고 서비스 객체를 제공

object Img {    // 객체 - Retrofit 인스턴스를 생성하고 설정
    private val baseUrl = "http://192.168.45.155:8000"

    private val retrofit = Retrofit.Builder()    // retrofit 인스턴스 생성
        .baseUrl(baseUrl)  //  API 서버가 실행 중인 컴퓨터의 IP 주소를 기본 url로 지정
        .addConverterFactory(GsonConverterFactory.create())
        // GsonConverterFactory를 생성해서 변환기로 등록 (json을 java 객체로 변환하기 위한 Gson)
        .build()

    val service get() = retrofit.create(ImageFileService::class.java)
    // ImageFileService 인터페이스를 구현한 서비스 객체를 생성하는 프로퍼티

    fun requestImageList(page: Int) = service.requestImageList(page)
}
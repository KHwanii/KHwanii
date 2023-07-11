package com.example.smarthome.ui.smartCam.service

import com.example.smarthome.ui.smartCam.data.Images
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// 안드로이드 앱에서 Retrofit 라이브러리를 통해 서버와 통신하기 위한 코드
// 실제 데이터를 가져와

// ImageFileService는 API 요청을 정의

interface ImageFileService {     // RESTful API 요청을 정의하는 인터페이스
    @GET("/api/image_file/")        // retrofit의 get 이노테이션 사용- GET 요청
    fun requestImageList(@Query("page") page: Int): Call<Images>
    // page라는 쿼리 매개변수를 받아서 이미지 목록을 가져오는 API 요청을 정의
}

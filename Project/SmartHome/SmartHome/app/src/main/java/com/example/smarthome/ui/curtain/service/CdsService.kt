package com.example.smarthome.ui.curtain.service

import com.example.smarthome.ui.curtain.data.Cds
import com.example.smarthome.ui.curtain.data.CurtainControl
import com.example.smarthome.ui.curtain.data.CurtainControlResponse
import com.example.smarthome.ui.curtain.data.CurtainTime
import com.example.smarthome.ui.curtain.data.CurtainTimeResponse

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CdsService {
    @GET("/api/cds/")
    fun requestCds(@Query("page") page: Int): Call<Cds>

    // api/cds_sensor/ 로 GET 요청을 보내는 requestCds 정의
    // 데이터를 페이지 별로 가져와

    @GET("/api/control_curtain/")
    fun curtainStatus() : Call<CurtainControlResponse>  // API서버의 현재 control 상태를 읽어오기

    @POST("/api/control_curtain/")
    fun controlCurtain(@Body control: CurtainControl): Call<Void> // 앱에서 제어한 control 상태를 API 서버에 업로드하기 (POST)

    @GET("/api/curtain_time/")
    suspend fun getCurtainTime() : Response<CurtainTimeResponse>  // API서버의 현재 control 상태를 읽어오기

    @POST("/api/curtain_time/")
    suspend fun setCurtainTime(@Body curtainTime: CurtainTime): Response<Void> // 앱에서 제어한 curtainTime 상태를 API 서버에 업로드하기 curtainTime(time_day, time_night)

}

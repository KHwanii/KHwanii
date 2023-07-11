package com.example.smarthome.ui.tempHumi.data.humi.service

import com.example.smarthome.ui.tempHumi.data.humi.DehumidifierControl
import com.example.smarthome.ui.tempHumi.data.humi.DehumidifierControlResponse
import com.example.smarthome.ui.tempHumi.data.humi.HumidifierControl
import com.example.smarthome.ui.tempHumi.data.humi.HumidifierControlResponse
import com.example.smarthome.ui.tempHumi.data.humi.Humidity
import com.example.smarthome.ui.tempHumi.data.humi.StandardHumi
import com.example.smarthome.ui.tempHumi.data.humi.StandardHumiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HumiService {
    @GET("/api/humi/")
    fun requestHumi(@Query("page") page: Int): Call<Humidity>     // API 서버에서 습도 상태 불러오기

    @GET("/api/standard_humi/")
    fun requestStdHumi(): Call<StandardHumiResponse>     // API 서버에서 습도 상태 불러오기

    @POST("/api/standard_humi/")
    fun postStandardHumi(@Body standard_humi : StandardHumi) : Call<Void>  // 앱에서 제어한 기준 습도 API 서버에 업데이트

    @GET("/api/control_dehumidifier/")
    fun dehumidifierStatus() : Call<DehumidifierControlResponse>  // API 서버에서 현재 제습기 제어상태 불러오기

    @POST("/api/control_dehumidifier/")
    fun controlDehumidifier(@Body control : DehumidifierControl) : Call<Void>  //  앱에서 제어한 제습기 상태 API 서버에 업데이트

    @GET("/api/control_humidifier/")
    fun humidifierStatus() : Call<HumidifierControlResponse>     // API 서버에서 현재 가습기 제어상태 불러오기

    @POST("/api/control_humidifier/")
    fun controlHumidifier(@Body control : HumidifierControl)  : Call<Void>    // 앱에서 제어한 가습기 상태 API 서버에 업데이트 (앱에서 가습기상태 POST 해서 제어)
}
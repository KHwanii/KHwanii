package com.example.smarthome.ui.tempHumi.data.temp.service

import com.example.smarthome.ui.tempHumi.data.temp.AirconControl
import com.example.smarthome.ui.tempHumi.data.temp.AirconControlResponse
import com.example.smarthome.ui.tempHumi.data.temp.HeaterControl
import com.example.smarthome.ui.tempHumi.data.temp.HeaterControlResponse
import com.example.smarthome.ui.tempHumi.data.temp.SmartControl
import com.example.smarthome.ui.tempHumi.data.temp.SmartControlResponse
import com.example.smarthome.ui.tempHumi.data.temp.StandardTemp
import com.example.smarthome.ui.tempHumi.data.temp.StandardTempResponse
import com.example.smarthome.ui.tempHumi.data.temp.Temperature

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface TempService {
    @GET("/api/temp/")
    fun requestTemp(@Query("page") page: Int): Call<Temperature>    // API 서버에서 온도 상태 불러오기

    @GET("/api/standard_temp/")
    fun requestStdTemp(): Call<StandardTempResponse>    // API 서버에서 온도 상태 불러오기

    @POST("/api/standard_temp/")
    fun postStandardTemp(@Body standard_temp : StandardTemp) : Call<Void>  // 앱에서 제어한 기준 온도 API 서버에 업데이트

    @GET("/api/control_heater/")
    fun heaterStatus() : Call<HeaterControlResponse>                  // API 서버에서 현재 히터 상태 불러오기

    @POST("/api/control_heater/")
    fun controlHeater(@Body control : HeaterControl) : Call<Void>     // 앱에서 제어한 히터 상태 API 서버에 업데이트(앱에서 히터상태 POST 해서 제어)

    @GET("/api/control_aircon/")
    fun airconStatus() : Call<AirconControlResponse>                 // API 서버에서 현재 에어컨 상태 불러오기

    @POST("/api/control_aircon/")
    fun controlAircon(@Body control : AirconControl) : Call<Void>    // 앱에서 에어컨상태 POST 해서 제어

    @GET("/api/total_control/")
    fun smartControlStatus() : Call<SmartControlResponse>                 // API 서버에서 현재 스마트제어 상태 불러오기

    @POST("/api/total_control/")
    fun smartControl(@Body control : SmartControl) : Call<Void>    // 앱에서 스마트제어 상태 POST 해서 on/off
}

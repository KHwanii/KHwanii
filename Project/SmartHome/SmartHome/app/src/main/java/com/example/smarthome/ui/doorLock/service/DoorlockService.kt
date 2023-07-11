package com.example.smarthome.ui.curtain.service


import com.example.smarthome.ui.doorLock.data.DoorlockControl
import com.example.smarthome.ui.doorLock.data.DoorlockControlResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DoorlockService {
    @GET("/api/control_doorlock/")
    fun DoorlockStatus() : Call<DoorlockControlResponse>  // API서버의 현재 control 상태를 읽어오기

    @POST("/api/control_doorlock/")
    fun controlDoorlock(@Body control: DoorlockControl): Call<Void>  // 앱에서 제어한 control 상태를 API 서버에 업로드하기

}

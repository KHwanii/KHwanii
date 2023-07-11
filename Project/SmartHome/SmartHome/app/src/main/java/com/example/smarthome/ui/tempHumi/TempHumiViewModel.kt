package com.example.smarthome.ui.tempHumi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.smarthome.ui.tempHumi.data.humi.Humidity
import com.example.smarthome.ui.tempHumi.data.humi.service.HUMI
import com.example.smarthome.ui.tempHumi.data.temp.Temperature
import com.example.smarthome.ui.tempHumi.data.temp.service.TEMP

import com.example.smarthome.ui.tempHumi.data.humi.DehumidifierControl
import com.example.smarthome.ui.tempHumi.data.humi.DehumidifierControlResponse
import com.example.smarthome.ui.tempHumi.data.humi.HumidifierControl
import com.example.smarthome.ui.tempHumi.data.humi.HumidifierControlResponse
import com.example.smarthome.ui.tempHumi.data.humi.StandardHumi
import com.example.smarthome.ui.tempHumi.data.humi.StandardHumiResponse

import com.example.smarthome.ui.tempHumi.data.temp.AirconControl
import com.example.smarthome.ui.tempHumi.data.temp.AirconControlResponse
import com.example.smarthome.ui.tempHumi.data.temp.HeaterControl
import com.example.smarthome.ui.tempHumi.data.temp.HeaterControlResponse
import com.example.smarthome.ui.tempHumi.data.temp.SmartControl
import com.example.smarthome.ui.tempHumi.data.temp.SmartControlResponse
import com.example.smarthome.ui.tempHumi.data.temp.StandardTemp
import com.example.smarthome.ui.tempHumi.data.temp.StandardTempResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TempHumiViewModel : ViewModel() {

    private var latestTemp: MutableLiveData<String> = MutableLiveData()
    private var latestHumi: MutableLiveData<String> = MutableLiveData()
    private var standardTempNow : MutableLiveData<String> = MutableLiveData()
    private var standardHumiNow : MutableLiveData<String> = MutableLiveData()
    private var latestCreatedAt: MutableLiveData<String> = MutableLiveData() // 온도, 습도, 생성일자 실시간 라이브 데이터

    private var isHeaterOn: MutableLiveData<Boolean> = MutableLiveData()
    private var isAirconOn: MutableLiveData<Boolean> = MutableLiveData()
    private var isDehumidifierOn: MutableLiveData<Boolean> = MutableLiveData()
    private var isHumidifierOn: MutableLiveData<Boolean> = MutableLiveData()        // 각 기기별 동작상태 실시간 라이브 데이터
    private var isSmartControlOn : MutableLiveData<Boolean> = MutableLiveData()

    // 데이터를 받아오고 사용하는 함수들 구성
    fun getLatestTemp(): LiveData<String> {
        return latestTemp
    }

    fun getLatestHumi(): LiveData<String> {
        return latestHumi
    }

    fun getLatestCreatedAt(): LiveData<String> {
        return latestCreatedAt
    }

    fun getStandardTemp() : LiveData<String> {
        return standardTempNow
    }

    fun getStandardHumi() : LiveData<String> {
        return standardHumiNow
    }

    fun getHeaterOn(): LiveData<Boolean> {
        return isHeaterOn
    }

    fun getAirconOn(): LiveData<Boolean> {
        return isAirconOn
    }

    fun getDehumidifierOn(): LiveData<Boolean> {
        return isDehumidifierOn
    }

    fun getHumidifierOn(): LiveData<Boolean> {
        return isHumidifierOn
    }

    fun getSmartConrtolOn(): LiveData<Boolean> {
        return isSmartControlOn
    }
    // 데이터를 받아오고 사용하는 함수들 구성


    fun loadLatestTemp() {
        val tempStatus = TEMP.service.requestTemp(page=1)
        tempStatus.enqueue(object : Callback<Temperature> {
            override fun onResponse(call: Call<Temperature>, response: Response<Temperature>) {
                if (response.isSuccessful) {
                    val temperatureData = response.body()
                    temperatureData?.let { data ->
                        data.results.firstOrNull()?.let { result ->
                            latestTemp.value = result.value.toString()
                            latestCreatedAt.value = result.createdAt
                        } ?: run {
                            latestTemp.value = "온도 데이터 수신 중"
                        }
                    }
                } else {
                    latestTemp.value = "네트워크 오류"
                }
            }

            override fun onFailure(call: Call<Temperature>, t: Throwable) {
                latestTemp.value = "네트워크 오류"
            }
        })
    }                // Temp 현재 상태(데이터) 불러오기 함수


    fun loadLatestHumi() {
        val humiStatus = HUMI.service.requestHumi(page=1)
        humiStatus.enqueue(object : Callback<Humidity> {
            override fun onResponse(call: Call<Humidity>, response: Response<Humidity>) {
                if (response.isSuccessful) {
                    val humidityData = response.body()
                    humidityData?.let { data ->
                        data.results.firstOrNull()?.let { result ->
                            latestHumi.value = result.value.toString()
                            latestCreatedAt.value = result.createdAt
                        } ?: run {
                            latestHumi.value = "온도 데이터 수신 중"
                        }
                    }
                } else {
                    latestHumi.value = "네트워크 오류"
                }
            }

            override fun onFailure(call: Call<Humidity>, t: Throwable) {
                latestHumi.value = "네트워크 오류"
            }
        })
    }                      // Humi 현재 상태(데이터) 불러오기 함수

    fun loadStandardTemp() {
        val statusCall = TEMP.service.requestStdTemp()
        statusCall.enqueue(object : Callback<StandardTempResponse> {
            override fun onResponse(call: Call<StandardTempResponse>, response: Response<StandardTempResponse>) {
                if (response.isSuccessful) {
                    val stdtempData = response.body()?.results?.firstOrNull()
                    standardTempNow.value = stdtempData?.standard_temp.toString()
                }
                else {
                    standardTempNow.value = "네트워크 에러"
                }
            }

            override fun onFailure(call: Call<StandardTempResponse>, t: Throwable) {
                standardTempNow.value = (25.0).toString()
            }
        })
    }


    fun setStandardTemp(standardTemp: Double) {
        val stdTemp = StandardTemp(standardTemp)
        val tempSetCall = TEMP.service.postStandardTemp(stdTemp)
        tempSetCall.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    standardTempNow.value = standardTemp.toString()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TempHumiViewModel", "네트워크 오류")
            }
        })
    }

    fun loadStandardHumi() {
        val statusCall = HUMI.service.requestStdHumi()
        statusCall.enqueue(object : Callback<StandardHumiResponse> {
            override fun onResponse(call: Call<StandardHumiResponse>, response: Response<StandardHumiResponse>) {
                if (response.isSuccessful) {
                    val stdhumiData = response.body()?.results?.firstOrNull()
                    standardHumiNow.value = stdhumiData?.standard_humi.toString()
                }
                else {
                    standardHumiNow.value = "네트워크 에러"
                }
            }

            override fun onFailure(call: Call<StandardHumiResponse>, t: Throwable) {
                standardHumiNow.value = (55).toString()
            }
        })
    }


    fun setStandardHumi(standardHumi : Double) {
        val stdHumi = StandardHumi(standardHumi)
        val humiSetCall = HUMI.service.postStandardHumi(stdHumi)
        humiSetCall.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    standardHumiNow.value = standardHumi.toString()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TempHumiViewModel", "네트워크 오류")
            }
        })
    }


    fun loadHeaterStatus() {
        val statusCall = TEMP.service.heaterStatus()
        statusCall.enqueue(object : Callback<HeaterControlResponse> {
            override fun onResponse(call: Call<HeaterControlResponse>, response: Response<HeaterControlResponse>) {
                if (response.isSuccessful) {
                    val heatercontrol = response.body()?.results?.firstOrNull()
                    isHeaterOn.value = heatercontrol?.control ?: false
                } else {
                    isHeaterOn.value = false
                }
            }

            override fun onFailure(call: Call<HeaterControlResponse>, t: Throwable) {
                isHeaterOn.value = false
            }
        })
    }      // 히터 현재 상태(데이터) 불러오기 함수

    fun toggleHeaterState(isOn: Boolean) {
        val control = HeaterControl(isOn)
        val controlCall = TEMP.service.controlHeater(control)
        controlCall.enqueue(object :Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isHeaterOn.value = isOn
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TempHumiViewModel", "네트워크 오류")
            }   // 스위치 통신이 실패한 경우 (네트워크 연결 문제)
        })
    }                 // 히터 상태를 서버에 업로드(업데이트) 하는 함수. 스위치와 서버 연결하는 역할



    fun loadAirconStatus() {
        val statusCall = TEMP.service.airconStatus()
        statusCall.enqueue(object : Callback<AirconControlResponse> {
            override fun onResponse(call: Call<AirconControlResponse>, response: Response<AirconControlResponse>) {
                if (response.isSuccessful) {
                    val airconcontrol = response.body()?.results?.firstOrNull()
                    isAirconOn.value = airconcontrol?.control ?: false
                }
                else {
                    isAirconOn.value = false
                }
            }

            override fun onFailure(call: Call<AirconControlResponse>, t: Throwable) {
                isAirconOn.value = false
            }
        })
    }      // 에어컨 현재 상태(데이터) 불러오기 함수

    fun toggleAirconState(isOn: Boolean) {
        val control = AirconControl(isOn)
        val controlCall = TEMP.service.controlAircon(control)
        controlCall.enqueue(object :Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isAirconOn.value = isOn
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TempHumiViewModel", "네트워크 오류")
            }   // 스위치 통신이 실패한 경우 (네트워크 연결 문제)
        })
    }      // 에어컨 상태를 서버에 업로드(업데이트) 하는 함수. 스위치와 서버 연결하는 역할


    fun loadDehumidifierStatus() {
        val statusCall = HUMI.service.dehumidifierStatus()
        statusCall.enqueue(object : Callback<DehumidifierControlResponse> {
            override fun onResponse(call: Call<DehumidifierControlResponse>, response: Response<DehumidifierControlResponse>) {
                if (response.isSuccessful) {
                    val dehumidifiercontrol = response.body()?.results?.firstOrNull()
                    isDehumidifierOn.value = dehumidifiercontrol?.control ?: false
                } else {
                    isDehumidifierOn.value = false
                }
            }

            override fun onFailure(call: Call<DehumidifierControlResponse>, t: Throwable) {
                isDehumidifierOn.value = false
            }
        })
    }      // 제습기 현재 상태(데이터) 불러오기 함수

    fun toggleDehumidifierState(isOn: Boolean) {
        val control = DehumidifierControl(isOn)
        val controlCall = HUMI.service.controlDehumidifier(control)
        controlCall.enqueue(object :Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isDehumidifierOn.value = isOn
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TempHumiViewModel", "네트워크 오류")
            }   // 스위치 통신이 실패한 경우 (네트워크 연결 문제)
        })
    }     // 제습기 상태를 서버에 업로드(업데이트) 하는 함수. 스위치와 서버 연결하는 역할



    fun loadHumidifierStatus() {
        val statusCall = HUMI.service.humidifierStatus()
        statusCall.enqueue(object : Callback<HumidifierControlResponse> {
            override fun onResponse(call: Call<HumidifierControlResponse>, response: Response<HumidifierControlResponse>) {
                if (response.isSuccessful) {
                    val humidifiercontrol = response.body()?.results?.firstOrNull()
                    isHumidifierOn.value = humidifiercontrol?.control ?: false
                } else {
                    isHumidifierOn.value = false
                }
            }

            override fun onFailure(call: Call<HumidifierControlResponse>, t: Throwable) {
                isHumidifierOn.value = false
            }
        })
    }      // 가습기 현재 상태(데이터) 불러오기 함수

    fun toggleHumidifierState(isOn: Boolean) {
        val control = HumidifierControl(isOn)
        val controlCall = HUMI.service.controlHumidifier(control)
        controlCall.enqueue(object :Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isHumidifierOn.value = isOn
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TempHumiViewModel", "네트워크 오류")
            }   // 스위치 통신이 실패한 경우 (네트워크 연결 문제)
        })
    }     // 가습기 상태를 서버에 업로드(업데이트) 하는 함수. 스위치와 서버 연결하는 역할


    fun loadSmartControlStatus() {
        val statusCall = TEMP.service.smartControlStatus()
        statusCall.enqueue(object : Callback<SmartControlResponse> {
            override fun onResponse(call: Call<SmartControlResponse>, response: Response<SmartControlResponse>) {
                if (response.isSuccessful) {
                    val smartcontrol = response.body()?.results?.firstOrNull()
                    isSmartControlOn.value = smartcontrol?.control ?: false
                }
                else {
                    isSmartControlOn.value = false
                }
            }

            override fun onFailure(call: Call<SmartControlResponse>, t: Throwable) {
                isSmartControlOn.value = false
            }
        })
    }      // 스마트컨트롤 제어 상태(데이터) 불러오기 함수

    fun toggleSmartControlState(isOn: Boolean) {
        val control = SmartControl(isOn)
        val controlCall = TEMP.service.smartControl(control)
        controlCall.enqueue(object :Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isSmartControlOn.value = isOn
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TempHumiViewModel", "네트워크 오류")
            }   // 스위치 통신이 실패한 경우 (네트워크 연결 문제)
        })
    }      // 스마트컨트롤 제어 상태를 서버에 업로드(업데이트) 하는 함수. 스위치와 서버 연결하는 역할


}
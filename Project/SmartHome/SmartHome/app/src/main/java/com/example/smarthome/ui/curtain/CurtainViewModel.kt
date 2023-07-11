package com.example.smarthome.ui.curtain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.smarthome.ui.curtain.data.Cds
import com.example.smarthome.ui.curtain.data.CurtainControl
import com.example.smarthome.ui.curtain.data.CurtainControlResponse
import com.example.smarthome.ui.curtain.data.CurtainTime
import com.example.smarthome.ui.curtain.service.CDS
import kotlinx.coroutines.launch


class CurtainViewModel : ViewModel() {            // 프래그먼트에 필요한 함수들을 ViewModel에서 관리. 클래스 같은 역할

    private var latestCds: MutableLiveData<String> = MutableLiveData()         // 최신Cds 변수 선언 및 라이브(실시간)데이터로 확인
    private var latestCreatedAt: MutableLiveData<String> = MutableLiveData()   // 최신CreatedAt 변수 선언 및 라이브(실시간)데이터로 확인
    private var isCurtainOpen: MutableLiveData<Boolean> = MutableLiveData()   // 라이브(실시간)데이터 변수 선언

    // LiveData for CurtainTime
    private val _curtainTime = MutableLiveData<CurtainTime>()
    val curtainTime: LiveData<CurtainTime>
        get() = _curtainTime


    fun getLatestCds(): LiveData<String> {
        return latestCds
    }

    fun getCurtainOpen(): LiveData<Boolean> {
        return isCurtainOpen
    }                                                    // 실시간 데이터를 받는 함수들


    fun loadLatestCds() {
        val cdsStatus = CDS.service.requestCds(page=1)   // CDS의 service의 requestCds 함수 사용해서 변수 선언하고, 데이터를 1페이지부터 불러오기
        cdsStatus.enqueue(object : Callback<Cds> {
            override fun onResponse(call: Call<Cds>, response: Response<Cds>) {
                if (response.isSuccessful) {
                    val brightnessData = response.body()   // 가져온 데이터
                    brightnessData?.let { data ->     //
                        data.results.firstOrNull()?.let { // it은 첫번쨰 결과물 -> CDS의 실제 데이터 (value, place)..등등
                            latestCds.value = it.value.toString()
                            latestCreatedAt.value = it.createdAt
                        } ?: run {
                            latestCds.value = "밝기 데이터 수신 중"
                        }
                    }
                }
                else {
                    latestCds.value = "네트워크 오류"
                }
            }

            override fun onFailure(call: Call<Cds>, t: Throwable) {
                latestCds.value = "네트워크 오류"
            }
        })
    }                                              // 실시간 데이터를 불러오는 함수들

    fun loadCurtainStatus() {
        val statusCall = CDS.service.curtainStatus()
        statusCall.enqueue(object : Callback<CurtainControlResponse> {
            override fun onResponse(call: Call<CurtainControlResponse>, response: Response<CurtainControlResponse>) {
                if (response.isSuccessful) {
                    val curtaincontrol = response.body()?.results?.firstOrNull()
                    isCurtainOpen.value = curtaincontrol?.control ?: false
                }
                else {
                    isCurtainOpen.value = false
                }
            }

            override fun onFailure(call: Call<CurtainControlResponse>, t: Throwable) {
                isCurtainOpen.value = false
            }
        })
    }                                       // 실시간 커텐 상태를 불러오는 함수



    fun toggleCurtainState(isOpen: Boolean) {   // 앱에서 스위치 상태를 변경하면 그것을 감지하고, API 서버에 그 데이터를 업로드하는 함수
        val control = CurtainControl(isOpen)
        val controlCall = CDS.service.controlCurtain(control)
        controlCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    isCurtainOpen.value = isOpen
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CurtainViewModel", "네트워크 오류")
            }   // 스위치 통신이 실패한 경우 (네트워크 연결 문제)
        })
    }                           // 커튼 스위치 변경 및 제어 함수




    fun getCurtainTime() {
        viewModelScope.launch {
            val response = CDS.service.getCurtainTime()
            if (response.isSuccessful) {
                _curtainTime.value = response.body()?.results?.get(0)
            }
            else {
                // TODO: handle the error
            }
        }
    }

    fun setCurtainTime(curtainTime: CurtainTime) {
        viewModelScope.launch {
            val response = CDS.service.setCurtainTime(curtainTime)
            if (response.isSuccessful) {
                // TODO: Update the curtainTime
            }
            else {
                // TODO: handle the error
            }
        }
    }

}
package com.example.smarthome.ui.doorLock

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smarthome.ui.curtain.CurtainFragment
import com.example.smarthome.ui.curtain.service.Doorlock
import com.example.smarthome.ui.doorLock.data.DoorlockControl
import com.example.smarthome.ui.doorLock.data.DoorlockControlResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoorLockViewModel : ViewModel() {

    private val isDoorOpen: MutableLiveData<Boolean> = MutableLiveData()

    fun getDoorlockOpen(): LiveData<Boolean> {
        return isDoorOpen
    }

    fun loadDoorlockStatus() {
        val statusCall = Doorlock.service.DoorlockStatus()
        statusCall.enqueue(object : Callback<DoorlockControlResponse> {
            override fun onResponse(
                call: Call<DoorlockControlResponse>,
                response: Response<DoorlockControlResponse>
            ) {
                if (response.isSuccessful) {
                    val doorlockcontrol = response.body()?.results?.firstOrNull()
                    isDoorOpen.value = doorlockcontrol?.control ?: false
                } else {
                    isDoorOpen.value = false
                }
            }

            override fun onFailure(call: Call<DoorlockControlResponse>, t: Throwable) {
                isDoorOpen.value = false
            }
        })
    }

    fun toggleDoorlockState(isOpen: Boolean) {
        val control = DoorlockControl(isOpen)
        val controlcall = Doorlock.service.controlDoorlock(control)

        controlcall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("CurtainFragment", "스위치 제어상태 업로드 성공")
                }
                else {
                    Log.e("CurtainFragment", "스위치 제어상태 업로드 실패")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CurtainFragment", "네트워크 오류")
            }
        })
    }
}
package com.example.smarthome.ui.doorLock

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentDoorlockBinding

class DoorLockFragment : Fragment() {

    companion object ToastManager {
        private var currentToast: Toast? = null  // 현재 토스트메세지가 Null 인지 확인

        fun showToast(context: Context?, message: String) {  // toast.makeText().show()의 역할
            currentToast?.cancel()                // 기존의 토스트메세지를 삭제
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }                   // 토스트 메세지 제어용 객체

    private var _binding : FragmentDoorlockBinding? = null
    private val mbinding get() = _binding!!  // 바인딩 처리

    private lateinit var doorImageView: ImageView
    private var isDoorOpen : Boolean = false          // isDoorOpen 변수 선언

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDoorlockBinding.inflate(inflater, container, false)
        val root: View = mbinding.root

        doorImageView = mbinding.imgDoorStatus

        return root
    } // onCreateView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doorlockViewModel : DoorLockViewModel by viewModels()   // Doorlock 뷰 모델 불러오기

        doorlockViewModel.getDoorlockOpen().observe(viewLifecycleOwner) { isOpen ->   // 뷰 모델 참조
            updateDoorlockUI(isOpen)
        }

        doorlockViewModel.loadDoorlockStatus()         // 뷰 모델 참조

        mbinding.btnDoorlock.setOnLongClickListener {
            isDoorOpen = !isDoorOpen
            updateDoorlockUI(isDoorOpen)
            doorlockViewModel.toggleDoorlockState(isDoorOpen)               // 뷰 모델 참조
            true
        }

    }   // onViewCreated



    fun updateDoorlockUI(isDoorOpen: Boolean) {
        if (isDoorOpen) {
            doorImageView.setImageResource(R.drawable.door_open)
            mbinding.txtDoorStatus.text = "문이 열려있습니다."
            showToast(context, "문이 열렸습니다.")                  // 문 열림 상태
        }
        else {
            doorImageView.setImageResource(R.drawable.door_close)
            mbinding.txtDoorStatus.text = "문이 닫혀있습니다."
            showToast(context, "문이 닫혔습니다.")                  // 문 닫힘 상태
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null    // 바인딩 자체를 메모리에서 null로 날려
    }

}
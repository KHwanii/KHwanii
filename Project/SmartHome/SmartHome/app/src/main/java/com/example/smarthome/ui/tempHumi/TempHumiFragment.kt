package com.example.smarthome.ui.tempHumi

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentTempHumiBinding

class TempHumiFragment : Fragment() {

    companion object ToastManager {
        private var currentToast: Toast? = null  // 현재 토스트메세지가 Null 인지 확인

        fun showToast(context: Context?, message: String) {  // toast.makeText().show()의 역할
            currentToast?.cancel()                // 기존의 토스트메세지를 삭제
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }                   // 토스트 메세지 제어용 객체

    private var _binding : FragmentTempHumiBinding? = null
    private val mbinding get() = _binding!!  // 바인딩 처리

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTempHumiBinding.inflate(inflater, container, false)
        val root: View = mbinding.root

        return root
     }  // onCreateView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSmartControl = mbinding.btnSmartControl             // 스마트컨트롤 프래그먼트 이동을 위한 버튼 바인딩하기
        val temphumiViewModel : TempHumiViewModel by viewModels()  // TempHumi 뷰 모델 불러오기


        temphumiViewModel.getLatestTemp().observe(viewLifecycleOwner) { // 뷰 모델 참조
            mbinding.currentTemp.text = it
        }

        temphumiViewModel.getLatestHumi().observe(viewLifecycleOwner) { // 뷰 모델 참조
            mbinding.currentHumi.text = it
        }

        temphumiViewModel.getHeaterOn().observe(viewLifecycleOwner) {  // 뷰 모델 참조
            if (it) {
                mbinding.currentHeater.text = "켜짐"
            }
            else {
                mbinding.currentHeater.text = "꺼짐"
            }
        }

        temphumiViewModel.getAirconOn().observe(viewLifecycleOwner) {  // 뷰 모델 참조
            if (it) {
                mbinding.currentAircon.text = "켜짐"
            }
            else {
                mbinding.currentAircon.text = "꺼짐"
            }
        }

        temphumiViewModel.getDehumidifierOn().observe(viewLifecycleOwner) {   // 뷰 모델 참조
            if (it) {
                mbinding.currentDehumidifier.text = "켜짐"
            }
            else {
                mbinding.currentDehumidifier.text = "꺼짐"
            }
        }

        temphumiViewModel.getHumidifierOn().observe(viewLifecycleOwner) {   // 뷰 모델 참조
            if (it) {
                mbinding.currentHumidifier.text = "켜짐"
            }
            else {
                mbinding.currentHumidifier.text = "꺼짐"
            }
        }


        temphumiViewModel.loadLatestTemp()
        temphumiViewModel.loadLatestHumi()
        temphumiViewModel.loadHeaterStatus()
        temphumiViewModel.loadAirconStatus()
        temphumiViewModel.loadDehumidifierStatus()
        temphumiViewModel.loadHumidifierStatus()          // 뷰 모델 참조




        btnSmartControl.setOnClickListener {
            findNavController().navigate(R.id.action_TempHumidFragment_to_smartControlFragment)
        }           // 버튼 클릭 시 스마트 컨트롤 프래그먼트로 이동

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null    // 바인딩 자체를 메모리에서 null로 날려
    }

}
package com.example.smarthome.ui.tempHumi

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentSmartControlBinding
import java.lang.NumberFormatException


class SmartControlFragment : Fragment() {

    companion object ToastManager {
        private var currentToast: Toast? = null  // 현재 토스트메세지가 Null 인지 확인

        fun showToast(context: Context?, message: String) {  // toast.makeText().show()의 역할
            currentToast?.cancel()                // 기존의 토스트메세지를 삭제
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }                   // 토스트 메세지 제어용 객체

    private var _binding : FragmentSmartControlBinding? = null
    private val mbinding get() = _binding!!

    private var isAirconOn : Boolean = false          // isDeviceOn 변수 선언
    private var isHeaterOn : Boolean = false          // isHeaterOn 변수 선언
    private var isHumidifierOn : Boolean = false      // isHumidifierOn 변수 선언
    private var isDehumidifierOn : Boolean = false    // isDehumidifierOn 변수 선언
    private var isSmartControlOn : Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSmartControlBinding.inflate(inflater, container, false)
        val root : View = mbinding.root

        return root
    }  // onCreateView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnReturn = mbinding.btnReturn                       // 돌아가기 버튼
        val temphumiViewModel: TempHumiViewModel by viewModels() // TempHumi 뷰 모델 불러오기


        temphumiViewModel.getHeaterOn().observe(viewLifecycleOwner) {  // 뷰 모델 참조
            UpdateSwitchUI(it, mbinding.btnHeater)
        }

        temphumiViewModel.getAirconOn().observe(viewLifecycleOwner) {  // 뷰 모델 참조
            UpdateSwitchUI(it, mbinding.btnAircon)
        }

        temphumiViewModel.getDehumidifierOn().observe(viewLifecycleOwner) {   // 뷰 모델 참조
            UpdateSwitchUI(it, mbinding.btnDehumidifier)
        }

        temphumiViewModel.getHumidifierOn().observe(viewLifecycleOwner) {   // 뷰 모델 참조
            UpdateSwitchUI(it, mbinding.btnHumidifier)
        }



        temphumiViewModel.getStandardTemp().observe(viewLifecycleOwner) {
            mbinding.txtStdTempNow.text = it
        }



        temphumiViewModel.getStandardHumi().observe(viewLifecycleOwner) {
            mbinding.txtStdHumiNow.text = it
        }

        temphumiViewModel.getSmartConrtolOn().observe(viewLifecycleOwner) {
            if (it) {
                mbinding.txtSmartControl.text = "스마트 제어 켜짐"
            }
            else {
                mbinding.txtSmartControl.text = "스마트 제어 꺼짐"
            }
        }


        temphumiViewModel.loadSmartControlStatus()

        temphumiViewModel.loadStandardTemp()
        temphumiViewModel.loadStandardHumi()

        temphumiViewModel.loadHeaterStatus()
        temphumiViewModel.loadAirconStatus()
        temphumiViewModel.loadDehumidifierStatus()
        temphumiViewModel.loadHumidifierStatus()          // 뷰 모델 참조


        mbinding.btnAircon.setOnLongClickListener{
            val AirconSwitch = mbinding.btnAircon
            isAirconOn = !isAirconOn
            UpdateSwitchUI(isAirconOn, AirconSwitch)
            temphumiViewModel.toggleAirconState(isAirconOn)
            true
        }

        mbinding.btnHeater.setOnLongClickListener{
            val HeaterSwitch = mbinding.btnHeater
            isHeaterOn = !isHeaterOn
            UpdateSwitchUI(isHeaterOn, HeaterSwitch)
            temphumiViewModel.toggleHeaterState(isHeaterOn)
            true
        }

        mbinding.btnDehumidifier.setOnLongClickListener{
            val DehumidifierSwitch = mbinding.btnDehumidifier
            isDehumidifierOn = !isDehumidifierOn
            UpdateSwitchUI(isDehumidifierOn, DehumidifierSwitch)
            temphumiViewModel.toggleDehumidifierState(isDehumidifierOn)
            true
        }

        mbinding.btnHumidifier.setOnLongClickListener{
            val HumidifierSwitch = mbinding.btnHumidifier
            isHumidifierOn = !isHumidifierOn
            UpdateSwitchUI(isHumidifierOn, HumidifierSwitch)
            temphumiViewModel.toggleHumidifierState(isHumidifierOn)
            true
        }

        mbinding.btnSmartControl.setOnLongClickListener{
            val SmartControlSwitch = mbinding.btnSmartControl
            isSmartControlOn = !isSmartControlOn
            UpdateSwitchUI(isSmartControlOn, SmartControlSwitch)
            temphumiViewModel.toggleSmartControlState(isSmartControlOn)
            true
        }

        btnReturn.setOnClickListener {
            findNavController().navigate(R.id.action_smartControlFragment_to_TempHumidFragment)
        }           // 버튼 클릭 시 온습도 확인 프래그먼트로 이동


        mbinding.btnTempSet.setOnClickListener {
            val stdTempNow = temphumiViewModel.getStandardTemp().value?.toDouble() ?: 25.0    // stdTempNow 는 현재 설정된 값을 가져옴. 만약 설정값을 불러오지 못하는 경우는 25.0으로 설정.
            val inputValue : Double = try{ if(mbinding.stdTempInput.text.toString().isEmpty()) stdTempNow else mbinding.stdTempInput.text.toString().toDouble() }  // 만약 입력되는 s의 값이 비어있으면 stdTempNow를 할당, 값이 들어오면 Double형태로 변환
            catch (e: NumberFormatException) {
                mbinding.stdTempInput.error = "유효한 숫자를 입력하세요."
                return@setOnClickListener
            }               // 만약 숫자가 아닌 다른 형식을 입력하려는 경우, Exception 오류 발생
            if (inputValue in 18.0..28.0) {
                temphumiViewModel.setStandardTemp(inputValue)
                showToast(context, "기준온도가 ${inputValue}도 로 설정되었습니다.")
                mbinding.stdTempInput.text.clear() // 텍스트 필드의 값을 초기화
            }
            else {
                mbinding.stdTempInput.error = "18도와 28도 사이의 값을 입력하세요."
            }
        }

        mbinding.btnHumiSet.setOnClickListener {
            val stdHumiNow = temphumiViewModel.getStandardHumi().value?.toDouble() ?: 55.0    // stdTempNow 는 현재 설정된 값을 가져옴. 만약 설정값을 불러오지 못하는 경우는 55.0으로 설정.
            val inputValue : Double = try{ if(mbinding.stdHumiInput.text.toString().isEmpty()) stdHumiNow else mbinding.stdHumiInput.text.toString().toDouble() }  // 만약 입력되는 s의 값이 비어있으면 stdTempNow를 할당, 값이 들어오면 Double형태로 변환
            catch (e: NumberFormatException) {
                mbinding.stdHumiInput.error = "유효한 숫자를 입력하세요."
                return@setOnClickListener
            }               // 만약 숫자가 아닌 다른 형식을 입력하려는 경우, Exception 오류 발생
            if (inputValue in 0.0..100.0) {
                temphumiViewModel.setStandardHumi(inputValue)
                showToast(context, "기준습도가 ${inputValue}으로 설정되었습니다.")
                mbinding.stdHumiInput.text.clear() // 텍스트 필드의 값을 초기화
            }
            else {
                mbinding.stdHumiInput.error = "0과 100 사이의 값을 입력하세요."
            }
        }

    }  // onViewCreated


    private fun UpdateSwitchUI(isOn : Boolean, SwitchImage : ImageView) {
        if(isOn) {
            SwitchImage.setImageResource(R.drawable.switch_on)
        }
        else {
            SwitchImage.setImageResource(R.drawable.switch_off)
        }
    }
}

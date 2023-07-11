package com.example.smarthome.ui.curtain

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentCurtainBinding


class CurtainFragment : Fragment() {

    companion object ToastManager {
        private var currentToast: Toast? = null  // 현재 토스트메세지가 Null 인지 확인

        fun showToast(context: Context?, message: String) {  // toast.makeText().show()의 역할
            currentToast?.cancel()                // 기존의 토스트메세지를 삭제
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }                   // 토스트 메세지 제어용 객체

    private var _binding: FragmentCurtainBinding? = null           // 프래그먼트의 안정성을 위해서 우선 Null 선언
    private val mbinding get() = _binding!! // 변수 설정.

    private lateinit var curtainSwitch: SwitchCompat    // curtainSwitch 를 SwitchCompat으로 설정
    private var isCurtainOpen : Boolean = false // 이해를 돕기위한 isCurtainOpen 선언. 초기값 false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCurtainBinding.inflate(inflater, container, false)
        val root: View = mbinding.root   // 화면에 보이는 것들을 바인딩으로 붙여주는 것. (초기화)

        curtainSwitch = mbinding.CurtainSwitch
        isCurtainOpen = curtainSwitch.isChecked // curtainSwitch 의 체크 상태를 isCurtainOpen 으로 선언

        return root
    }            // onCreateView , 뷰 시작시 초기 설정 -> 화면에 보이는 것들

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // 뷰가 만들어 지고 본격적으로 실행할 코드들
        super.onViewCreated(view, savedInstanceState)

        val curtainViewModel: CurtainViewModel by viewModels()     // Curtain 뷰 모델 불러오기

        curtainViewModel.getLatestCds().observe(viewLifecycleOwner) { cds ->   // 뷰 모델 참조
            mbinding.currentCds.text = cds
        }

        curtainViewModel.getCurtainOpen().observe(viewLifecycleOwner) { isOpen ->   // 뷰 모델 참조
            updateCurtainUI(isOpen)
            curtainSwitch.isChecked = isOpen
        }

        curtainViewModel.loadLatestCds()                    //
        curtainViewModel.loadCurtainStatus()                 // 뷰 모델 참조

        curtainSwitch.setOnCheckedChangeListener { _, isChecked ->         // 뷰 모델 참조
            curtainViewModel.toggleCurtainState(isChecked)
        }
    }   // onViewCreated, 프래그먼트 생성시 실행할 것들


    fun updateCurtainUI(isCurtainOpen: Boolean) {
        if (isCurtainOpen) {
            mbinding.CurtainStatus.setImageResource(R.drawable.curtain_on)
            mbinding.CurtainSwitch.text = "커튼 닫기"
            showToast(context, "커튼이 열렸습니다.")                  // 커튼 열림 상태
        } else {
            mbinding.CurtainStatus.setImageResource(R.drawable.curtain_off)
            mbinding.CurtainSwitch.text = "커튼 열기"
            showToast(context, "커튼이 닫혔습니다.")                  // 커튼 닫힘 상태
        }
    }                           // 커튼 UI 업데이트 함수


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null                     // 바인딩 해제
    }  // onDestroyView, 프래그먼트 소멸시 실행할 것들

}
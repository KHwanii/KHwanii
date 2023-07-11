package com.example.smarthome.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.smarthome.databinding.FragmentHomeBinding

import com.example.smarthome.ui.curtain.CurtainViewModel
import com.example.smarthome.ui.doorLock.DoorLockViewModel
import com.example.smarthome.ui.tempHumi.TempHumiViewModel



class HomeFragment: Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val mbinding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = mbinding.root

        return root
    }         // 뷰 시작시 초기설정

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val temphumiViewModel: TempHumiViewModel by viewModels()
        val curtainViewModel: CurtainViewModel by viewModels()
        val doorlockViewModel: DoorLockViewModel by viewModels()            // 데이터 적용을 위해 각각 ViewModel 불러오기

        curtainViewModel.getLatestCds().observe(viewLifecycleOwner) { cds ->
            mbinding.currentCdsHome.text = cds
        }

        temphumiViewModel.getLatestTemp().observe(viewLifecycleOwner) { temp ->
            mbinding.currentTemp.text = temp
        }

        temphumiViewModel.getLatestHumi().observe(viewLifecycleOwner) { humi ->
            mbinding.currentHumi.text = humi
        }

        curtainViewModel.getCurtainOpen().observe(viewLifecycleOwner) { isOpen ->
            if (isOpen) {
                mbinding.currentCurtainHome.text = "열림"
            }
            else {
                mbinding.currentCurtainHome.text = "닫힘"
            }
        }

        doorlockViewModel.getDoorlockOpen().observe(viewLifecycleOwner) { isOpen ->
            if (isOpen) {
                mbinding.currentDoorlockHome.text = "열림"
            }
            else {
                mbinding.currentDoorlockHome.text = "닫힘"
            }
        }

        temphumiViewModel.loadLatestTemp()
        temphumiViewModel.loadLatestHumi()
        curtainViewModel.loadLatestCds()
        curtainViewModel.loadCurtainStatus()
        doorlockViewModel.loadDoorlockStatus()


    }  // onViewCreated

    override fun onDestroyView() {
        _binding = null    // 바인딩 자체를 메모리에서 null로 날려
        super.onDestroyView()
    }
}


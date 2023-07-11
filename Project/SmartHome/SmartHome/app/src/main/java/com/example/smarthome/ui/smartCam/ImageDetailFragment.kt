package com.example.smarthome.ui.smartCam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.smarthome.databinding.FragmentImageDetailBinding


const val ARG_URL = "http://192.168.45.155:8000/api/image_file/"   // 이미지 url 전달

// 선택된 이미지 화면에 표시

class ImageDetailFragment : Fragment() {
    private var imageUrl: String? = null    // 전달된 이미지 url 저장

    var _binding : FragmentImageDetailBinding? = null  // FragmentImageBinding 인스턴스 저장
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {  // Fragment가 생성될 때 호출
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_URL)    // ARG_URL 값을 가져와서 url 변수에 저장
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {  // 프래그먼트의 레이아웃을 생성
        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)   // FragmentVideoBinding을 인플레이트
        return binding.root       // 해당 view의 루트 반환
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {  // View가 생성된 후에 호출
        super.onViewCreated(view, savedInstanceState)

        imageUrl?.let {          // Glide로 imageUrl을 가져옴
            Glide.with(requireContext())
                .load(it)              // imageUrl에서 이미지를 로드해서
                .into(binding.imgFile)       // binding.imgFile 에 데이터를 붙이고 표시하기
        }
    }

    override fun onDestroy() {   // 프래그먼트가 파괴될 때 호출
        super.onDestroy()
        _binding = null       // View 바인딩 해제
    }
}
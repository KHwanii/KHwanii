package com.example.myapp.ui.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.myapp.databinding.FragmentBoardDetailBinding
import com.example.myapp.login.monthDayHourMinChange


class BoardDetailFragment : Fragment() {

    private var _binding: FragmentBoardDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBoardDetailBinding.inflate(inflater, container, false)

        val title = arguments?.getString("title")
        val category = arguments?.getString("category")
        val author = arguments?.getString("author")
        val createdDate = arguments?.getString("created_date")
        val content = arguments?.getString("content")
/*
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)       // 서버에서 받는 날짜 데이터 형식
        val targetFormat = SimpleDateFormat("M월 d일 HH시 mm분", Locale.KOREAN)                   // 어플에서 변환할 날짜 데이터 형식

        val date: Date = originalFormat.parse(createdDate)
        val formattedDate: String = targetFormat.format(date)
*/

        binding.detailTitle.text = title
        binding.detailCategory.text = category
        binding.detailAuthor.text = author
        binding.detailCreatedTime.text = monthDayHourMinChange(createdDate!!)       // 날짜 데이터 변환 함수
        // binding.detailCreatedTime.text = formattedDate
        binding.detailContent.text = content

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
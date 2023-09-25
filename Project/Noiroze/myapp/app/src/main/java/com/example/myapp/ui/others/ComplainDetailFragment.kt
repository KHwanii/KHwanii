package com.example.myapp.ui.others

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapp.R
import com.example.myapp.databinding.FragmentComplainDetailBinding
import com.example.myapp.login.monthDayHourMinChange
import com.example.myapp.login.monthDayHourMinFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ComplainDetailFragment : Fragment() {

    private var _binding : FragmentComplainDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentComplainDetailBinding.inflate(inflater, container, false)

        val title = arguments?.getString("title")
        val author = arguments?.getString("author")
        val content = arguments?.getString("content")
        val createdDate = arguments?.getString("created_date")
        // Log.d("ComplainDetailFragment", "${createdDate}")

        binding.complainCategory.text = title
        binding.complainAuthor.text = author
        binding.complainContent.text = content
        binding.complainCreatedTime.text = monthDayHourMinChange(createdDate!!)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.myapp.ui.community

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.myapp.R
import com.example.myapp.databinding.FragmentCommunityListBinding
import com.example.myapp.ui.community.ViewModel.CommunityListAdapter
import com.example.myapp.ui.community.ViewModel.CommunityListViewModel
import com.example.myapp.ui.community.service.CommunityResult

class CommunityListFragment : Fragment() {
    private var _binding: FragmentCommunityListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityListViewModel by viewModels()         // 뷰모델 선언
    private val adapter by lazy {                   // 어댑터 변수 선언.
        CommunityListAdapter { CommunityResult ->
            onItemClick(CommunityResult)                                           // 리사이클러 뷰 클릭에 대한 결과물을 넘겨줌
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCommunityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel 설정
        viewModel.commuList.observe(viewLifecycleOwner, Observer { commuList ->
            adapter.updateData(commuList)
        })

        // RecyclerView 어댑터 및 레이아웃 매니저 설정
        binding.BoardlistView.adapter = this.adapter                                    
        binding.BoardlistView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getCommuList(1, "전체보기")         // 최초 1페이지, 전체보기로 데이터 불러오기.
        initButtons()                                               // 버튼 설정 사용

    } // onViewCreated

    fun onItemClick(eachBoardList: CommunityResult) {
        val bundle = Bundle()
        bundle.putString("title", eachBoardList.title)
        bundle.putString("category", eachBoardList.category)
        bundle.putString("author", eachBoardList.author)
        bundle.putString("created_date", eachBoardList.created_date)
        bundle.putString("content", eachBoardList.content)
        findNavController().navigate(R.id.action_communityFragment_to_boardDetailFragment, bundle)
    }
    
    private fun initButtons() {
        val btnAll = binding.btnAll
        val btnCommu = binding.btnCommu
        val btnInfo = binding.btnInfo
        val btnPraise = binding.btnPraise
        val btnShare = binding.btnShare
        val btnAddBoard = binding.btnAddBoard

        btnAll.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btnAll, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btnAll.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btnAll, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            viewModel.getCommuList(1,"전체보기")
        }

        btnCommu.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btnCommu, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btnCommu.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btnCommu, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            viewModel.getCommuList(1,"소통해요")
        }

        btnInfo.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btnInfo, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btnInfo.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btnInfo, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            viewModel.getCommuList(1,"정보공유")
        }

        btnPraise.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btnPraise, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btnPraise.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btnPraise, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            viewModel.getCommuList(1,"칭찬해요")
        }

        btnShare.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btnShare, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btnShare.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btnShare, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            viewModel.getCommuList(1,"나눔해요")
        }

        btnAddBoard.setOnClickListener {
            findNavController().navigate(R.id.action_communityFragment_to_boardCreateFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.myapp.ui.community

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myapp.R
import com.example.myapp.databinding.FragmentCommunityBinding
import com.example.myapp.ui.community.service.Result
import com.example.myapp.databinding.ItemCommunityBoardBinding
import com.example.myapp.ui.community.service.CommunityBoardSetup
import com.example.myapp.ui.community.service.CommunityBoards
import com.example.myapp.ui.community.service.getTimeDifference

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CommunityBoardAdapter

    private val boardList = mutableListOf<Result>()
    private val filteredBoardList = mutableListOf<Result>()
    private var currentFilter: String? = null

    private var currentPage = 1
    private var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CommunityBoardAdapter() // 멤버 변수를 사용
        binding.BoardlistView.apply {
            adapter = this@CommunityFragment.adapter                   // 멤버 변수의 어댑터 사용
            layoutManager = LinearLayoutManager(requireContext())   // 레이아웃 매니저 사용
        }

        binding.BoardlistView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItemPosition + 1)) {
                    loadMoreCommunityData(currentPage)
                }
            }
        })
        loadCommunityData(currentPage)
        initButtonListeners()
    }  // onViewCreated

    private fun loadCommunityData(page: Int) {
        isLoading = true
        // api 호출 기본 골격 retrofit으로 서버에서 BoardList 목록을 가져옴
        val communityBoardList = CommunityBoardSetup.service.requestCommunityBoardList(page=1)                  // communityBoardList변수를 선언후, CommunityBoardSetup.service.requestCommunityBoardList 에서 첫번째 페이지를 가져오기.
        communityBoardList.enqueue(object : Callback<CommunityBoards> {                                     // retrofit2의 Callback 으로 <Images> 에서 데이터들을 Queue 로 가져옴
            @SuppressLint("NotifyDataSetChanged")                                                           // 경고 무시 코드.
            override fun onResponse(call: Call<CommunityBoards>, response: Response<CommunityBoards>) {     // CommunityBoards의 데이터를 Call 하고, Response로 응답을 확인
                if(response.isSuccessful) {                                                                 // 성공했을 때. 즉, 네트워크 응답으로 데이터가 앱으로 전달될 때.
                    val boardLists = response.body()                                                        // boardLists 라는 변수를 만들고, 여기에 응답에서 가져온 게시글 목록 데이터를 할당
                    // boardList.clear()                                                                       // 게시글 목록 초기화
                    // boardList.addAll(boardLists!!.results)                                                  // 게시글을 추가할 때마다 BoardList에 목록 추가. boardLists!!.results는 목록 데이터의 결과물들을 말함.
                    if (isAdded && _binding != null) {                                                      // 프래그먼트가 실행 및 활성화되고(액티비티에 추가되었는지 확인), 바인딩이 null이 아닐 때
                        adapter.setData(boardLists!!.results)                                                              // adapter에 변경사항이 있음을 알려줌
                    }
                }
                isLoading = false
            }

            override fun onFailure(call: Call<CommunityBoards>, t: Throwable) {
                Log.e("네트워크 요청 실패", t.toString())    // Log의 error 출력
            }
        })
    }

    private fun loadMoreCommunityData(page: Int) {
        isLoading = true

        // api 호출 기본 골격 retrofit으로 서버에서 BoardList 목록을 가져옴
        val communityBoardList = CommunityBoardSetup.service.requestCommunityBoardList(page=page) // communityBoardList변수를 선언후, CommunityBoardSetup.service.requestCommunityBoardList 에서 첫번째 페이지를 가져오기.
        communityBoardList.enqueue(object : Callback<CommunityBoards> { // retrofit2의 Callback 으로 <Images> 에서 데이터들을 Queue 로 가져옴
            @SuppressLint("NotifyDataSetChanged") // 경고 무시 코드.
            override fun onResponse(call: Call<CommunityBoards>, response: Response<CommunityBoards>) { // CommunityBoards의 데이터를 Call 하고, Response로 응답을 확인
                if(response.isSuccessful) { // 성공했을 때. 즉, 네트워크 응답으로 데이터가 앱으로 전달될 때.
                    val newBoardLists = response.body() // boardLists 라는 변수를 만들고, 여기에 응답에서 가져온 게시글 목록 데이터를 할당
                    if (isAdded && _binding != null) { // 프래그먼트가 실행 및 활성화되고(액티비티에 추가되었는지 확인), 바인딩이 null이 아닐 때
                        adapter.addData(newBoardLists!!.results) // adapter에 변경사항이 있음을 알려줌
                    }
                }
                isLoading = false
                currentPage++
            }

            override fun onFailure(call: Call<CommunityBoards>, t: Throwable) {
                Log.e("네트워크 요청 실패", t.toString()) // Log의 error 출력
            }
        })
    }



    inner class CommunityBoardAdapter : RecyclerView.Adapter<CommunityBoardAdapter.CommunityBoardViewHolder>() {
        fun addData(newData: List<Result>) {
            boardList.addAll(newData)
            filterData(currentFilter) // 현재 필터 상태를 기억하는 변수를 사용하여 데이터를 필터링합니다.
        }

        fun setData(newData: List<Result>) {
            boardList.clear()
            boardList.addAll(newData)

            filteredBoardList.clear()
            filteredBoardList.addAll(newData)

            notifyDataSetChanged()
        }

        fun filterData(category: String?) {
            currentFilter = category
            filteredBoardList.clear()

            if(category != null) {
                filteredBoardList.addAll(boardList.filter { it.category == category })
            }
            else {
                // 카테고리가 null이면 모든 데이터를 보여줍니다.
                filteredBoardList.addAll(boardList)
            }
            notifyDataSetChanged()
        }

        override fun getItemCount() = filteredBoardList.size

        inner class CommunityBoardViewHolder(val binding: ItemCommunityBoardBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityBoardViewHolder {
            val binding = ItemCommunityBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CommunityBoardViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CommunityBoardViewHolder, position: Int) {
            val eachBoardList = filteredBoardList[position]
            holder.binding.boardCategory.text = eachBoardList.category
            holder.binding.boardTitle.text = eachBoardList.title
            holder.binding.boardAuthor.text = eachBoardList.author
            holder.binding.boardCreated.text = getTimeDifference(eachBoardList.createdDate)
            holder.binding.boardLike.text = eachBoardList.like.size.toString()
            // holder.binding.boardReply.text = eachBoardList.reply

            holder.itemView.setOnClickListener {       // 목록에서 항목 클릭시, 여기서 _는 itemView의 View인데, 사용하지 않으므로 _로 처리.
                onItemClick(eachBoardList, position)                    // onItemClick 함수 사용. it은 위쪽의 imageList의 Result, position은 아이템의 위치.
            }
        }

        // 목록에서 항목을 클릭했을 때 호출되는 함수
        fun onItemClick(eachBoardList: Result, position: Int) {   // Result에서 가져온 imgFile이 목표, 몇번째 파일인지 position으로 확인을 함.
            val bundle = Bundle()
            bundle.putString("title", eachBoardList.title)
            bundle.putString("category", eachBoardList.category)
            bundle.putString("author", eachBoardList.author)
            bundle.putString("createdDate", eachBoardList.createdDate.toString())
            bundle.putString("content", eachBoardList.content)
            findNavController().navigate(R.id.action_communityFragment_to_boardDetailFragment, bundle)
        }

    }         // CommunityBoardAdapter

    private fun initButtonListeners() {
        val btn_All = binding.btnAll
        val btn_Commu = binding.btnCommu
        val btn_Info = binding.btnInfo
        val btn_Praise = binding.btnPraise
        val btn_share = binding.btnShare
        val btn_addBoard = binding.btnAddBoard


        btn_All.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btn_All, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btn_All.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btn_All, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            adapter.filterData(null)
        }

        btn_Commu.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btn_Commu, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btn_Commu.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btn_Commu, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            adapter.filterData("소통해요")
        }

        btn_Info.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btn_Info, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btn_Info.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btn_Info, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            adapter.filterData("정보공유")
        }

        btn_Praise.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btn_Praise, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btn_Praise.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btn_Praise, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            adapter.filterData("칭찬해요")
        }

        btn_share.setOnClickListener {
            val startColor = Color.parseColor("#FFFFFF")
            val endColor = Color.parseColor("#D1CFCF")
            val backgroundColorAnimator = ObjectAnimator.ofObject(btn_share, "backgroundColor", ArgbEvaluator(), startColor, endColor)
            backgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
            backgroundColorAnimator.start()

            btn_share.postDelayed({
                val revertBackgroundColorAnimator = ObjectAnimator.ofObject(btn_share, "backgroundColor", ArgbEvaluator(), endColor, startColor)
                revertBackgroundColorAnimator.duration = 100 // 변경 지속 시간 100ms
                revertBackgroundColorAnimator.start()
            }, 50L) // 지연시간 100ms (0.1초)

            adapter.filterData("나눔해요")
        }

        btn_addBoard.setOnClickListener {
            findNavController().navigate(R.id.action_communityFragment_to_boardCreateFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
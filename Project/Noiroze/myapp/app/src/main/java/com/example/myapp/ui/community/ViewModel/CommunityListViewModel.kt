package com.example.myapp.ui.community.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.myapp.ui.community.service.CommunityResult                            // CommunityBoard.kt 의 Result 가져오기
import com.example.myapp.ui.community.service.CommunityBoardSetup.service

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityListViewModel : ViewModel() {
    private val _commuList = MutableLiveData<List<CommunityResult>>() // BoardItem은 실제 데이터 모델 타입으로 변경해주세요.
    val commuList: LiveData<List<CommunityResult>> = _commuList
    
    fun getCommuList(page : Int, category: String) {
        val commuListCall = service.requestCommunityBoardList(page, category)      // 1페이지, 카테고리에 따라 받아오기
        commuListCall.enqueue(object : Callback<List<CommunityResult>> {
            override fun onResponse(call: Call<List<CommunityResult>>, response: Response<List<CommunityResult>>) {
                if(response.isSuccessful) {
                    val boardList = response.body()
                    if(boardList != null) {
                        _commuList.value = boardList!!
                    }
                }
                else {
                    Log.e("CommunityListViewModel", "목록 불러오기 실패: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<CommunityResult>>, t: Throwable) {
                Log.e("CommunityListViewModel", "네트워크 연결 에러 ${t.message}")
            }
        }) // enqueue
    }   // getCommuList 함수

    fun loadMorePage(currentPage : Int, category: String) {
        val nextPage = currentPage + 1
        getCommuList(nextPage, category)
    }   // loadMorePage 함수. currentPage, category 값을 받아서, 다음 페이지를 로드
}
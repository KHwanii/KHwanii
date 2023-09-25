package com.example.myapp.ui.others.ViewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.myapp.ui.others.service.ComplainBoards
import com.example.myapp.ui.others.service.NoticeComplain.compService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplainListViewModel : ViewModel() {
    private val _compList = MutableLiveData<ComplainBoards>()
    val compList : LiveData<ComplainBoards> = _compList

    fun getCompList(token: String?, page: Int) {
        val compListCall = compService.requestComplainList("Bearer $token", page)
        compListCall.enqueue(object : Callback<ComplainBoards> {
            override fun onResponse(call: Call<ComplainBoards>, response: Response<ComplainBoards>) {
                if (response.isSuccessful) {
                    val compList = response.body()
                    if (compList != null) {
                        // Log.d("ComplainListViewModel", "Response Body: $compList")
                        _compList.value = compList!!
                    }
                }
                else {
                    Log.e("ComplainListViewModel", "목록 불러오기 실패: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ComplainBoards>, t: Throwable) {
                Log.e("ComplainListViewModel", "네트워크 연결 에러 ${t.message}")
            }
        })  // enqueue
    }   // getCompList 함수

    fun loadMorePage(token: String?, currentPage : Int) {
        val nextPage = currentPage + 1
        getCompList(token, nextPage)
    }   // loadMorePage 함수. currentPage, category 값을 받아서, 다음 페이지를 로드
}

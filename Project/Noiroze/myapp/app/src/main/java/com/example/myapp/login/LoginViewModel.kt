package com.example.myapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    sealed class LoginState {
        data class Success(val user: LoginUser) : LoginState()
        data class Failure(val message: String) : LoginState()
    } // Login 상태를 나타내는 sealed class

    fun loginUser(userid: String, password: String) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(userid, password)               // LoginRequest 객체 생성
                val response = Login.service.userLogin(loginRequest)            // service의 로그인함수 변수 정의, Suspended함수
                if (response.isSuccessful && response.body() != null) {         // 로그인 함수처리 성공 && 로그인 데이터가 null이 아닌 경우
                    _loginState.value = LoginState.Success(response.body()!!)
                } else {
                    _loginState.value = LoginState.Failure("아이디와 비밀번호를 확인해 주세요.")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Failure(e.message ?: "알수없는 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.")
            }
        }
    }
} // LoginViewModel